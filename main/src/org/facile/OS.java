package org.facile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.facile.Facile.*;

import org.facile.ProcessIO.ProcessOut;

public class OS {
	
	static Class<OS> os = OS.class;

	private static final Logger log = log(os);

	public static DateFormat lstartFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

	
	public static enum ProcessState {
		Runnable,
		Sleep,
		Uninterruptible,
		Stopped,
		Zombie,
		Idle
	}
	
	
	public static class ProcessInfo {
		public float cpuUsage;
		public float memUsage;
		public int processId;
		public int parentProcessId;
		public int userId;
		public int groupId;
		public int elaspedTime;
		public int nice;
		public int priority;
		public ProcessState state;
		public String command;
		public String [] arguments;
		public Date lstart;
		@Override
		public String toString() {
			return "\nProcessInfo [cpuUsage=" + cpuUsage + ", memUsage="
					+ memUsage + ", processId=" + processId
					+ ", parentProcessId=" + parentProcessId + ", userId="
					+ userId + ", groupId=" + groupId + ", elaspedTime="
					+ elaspedTime + ", nice=" + nice + ", priority=" + priority
					+ ", state=" + state + ", command=" + command
					+ ", arguments=" + Arrays.toString(arguments) + "]";
		}
		
		
		
	}
	
	public static enum Signal {
		ALL, //0
		HUP, // (hang up) 1
	    INT, // (interrupt) 2
	    QUIT, //(quit) 3
	    NOP1, //4
	    NOP2, //5	    
	    ABRT, //(abort) 6
	    NOP3, //7
	    NOP4, //8
	    KILL, // 9 (non-catchable, non-ignorable kill)
	    NOP5, //10
	    NOP6, //11
	    NOP7, //12
	    NOP8, //13
	    ALRM // 14 (alarm clock)
	}

	public static int kill (int... processIds) {
		return kill(Signal.KILL, processIds);
	}
	
	public static int kill (Signal s, int... processIds) {
		return exec("kill -" + s.ordinal() +' '+ join(' ', processIds));
	}

	public static List<ProcessInfo> ps () {
		return ps(true);
	}
	public static List<ProcessInfo> ps (boolean all) {
		
		ProcessOut pout;
		if (all) {
			                                     //0     1   2   3    4   5   6     7   8-12    13   14    15    16
			pout = run("/bin/sh", "-c", "ps -Ao \"%cpu %mem pid ppid uid gid etime ni   lstart  pri  stat  comm  args\"");
		} else {
			pout = run("/bin/sh", "-c", "ps -o \"%cpu %mem pid ppid uid gid etime ni lstart pri stat comm args\"");			
		}
		String stdout = pout.stdout;
		
		if (pout.exit==0) {
			return extractPS(stdout);
		} else {
			warning(log, "ps did not execute correctly exit code was " + pout.exit);
			return Collections.<ProcessInfo>emptyList();
		}
	}

	
	public static class ElaspedTime {
		int day;
		int hours;
		int minutes;
		int seconds;
		
		
		public ElaspedTime() {

		}
		
		public ElaspedTime(int day, int hours, int minutes, int seconds) {
			super();
			this.day = day;
			this.hours = hours;
			this.minutes = minutes;
			this.seconds = seconds;
		}
		
		@Override
		public String toString() {
			return "ElaspedTime [day=" + day + ", hours=" + hours
					+ ", minutes=" + minutes + ", seconds=" + seconds + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + day;
			result = prime * result + hours;
			result = prime * result + minutes;
			result = prime * result + seconds;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ElaspedTime other = (ElaspedTime) obj;
			if (day != other.day)
				return false;
			if (hours != other.hours)
				return false;
			if (minutes != other.minutes)
				return false;
			if (seconds != other.seconds)
				return false;
			return true;
		}
		
		public int toInt() {
			return this.seconds * 1000 + this.minutes * 1000 * 60 + this.hours * 1000 * 60 * 60 +
					this.day * 1000 * 60 * 60 * 24;
		}
		
		
	}

	protected static ElaspedTime elaspedTime (String str) {
		Pattern re = Regex.re("/({digit}{digit}-)?({digit}{digit}:)?({digit}{digit}):({digit}{digit})/");
		Matcher matcher = re.matcher(str);
		boolean find = matcher.find();
		if (find) {
			ElaspedTime etime = new ElaspedTime();
			
			int groupCount = matcher.groupCount();

			String day = null;
			String hours = null;
			String minutes = null;
			String seconds = null;

			if (groupCount==4) {
				 day = matcher.group(1);
				 hours = matcher.group(2);
				 minutes = matcher.group(3);
				 seconds = matcher.group(4);
			} else if (groupCount==3) {
				 hours = matcher.group(1);
				 minutes = matcher.group(2);
				 seconds = matcher.group(3);
			} else if (groupCount==2) {
				 minutes = matcher.group(1);
				 seconds = matcher.group(2);
			} 
			
			
			if (day!=null) {
				etime.day = toInt(day);
			}
			if (hours!=null) {
				etime.hours = toInt(hours);
			}
			if (minutes!=null) {
				etime.minutes = toInt(minutes);
			}
			etime.seconds = toInt(seconds);
			return etime;
		}
		return null;
	}
	protected static List<ProcessInfo> extractPS(String stdout) {
		String[] lines = toLines(stdout);
		
		List<ProcessInfo> psLines = new ArrayList<OS.ProcessInfo>(lines.length);
		
		int index = 0;
		for (String line : lines) {
			
			if (index!=0) {
				
				String[] split = split(line);
				if (split.length < 5) {
					continue;
				}
				ProcessInfo pi = new ProcessInfo();
				pi.cpuUsage = toFloat(split[0]);
				pi.memUsage = toFloat(split[1]);
				pi.processId = toInt(split[2]);
				pi.parentProcessId = toInt(split[3]);
				pi.userId = toInt(split[4]);
				pi.groupId = toInt(split[5]);
				pi.elaspedTime = elaspedTime(split[6]).toInt();
				pi.nice = toInt(split[7]);
				
				
				try {
					String lstart = join(' ', split[8], split[9], split[10], split[11], split[12]);
					pi.lstart = lstartFormatter.parse(lstart);
				} catch (ParseException e) {
					warning(log, "Unable to parse lstart from ps command.");
				}

				pi.priority = toInt(split[13]);
				

				char stateChar = split[14].charAt(0);
				switch(stateChar) {
				case 'R' :
					pi.state = ProcessState.Runnable;
					break;
				case 'S' :
					pi.state = ProcessState.Sleep;
					break;
				case 'T' :
					pi.state = ProcessState.Stopped;
					break;
				case 'Z' :
					pi.state = ProcessState.Zombie;
					break;
				case 'D' :
					pi.state = ProcessState.Uninterruptible;
					break;
				case 'U' :
					pi.state = ProcessState.Uninterruptible;
					break;
				case 'I' :
					pi.state = ProcessState.Idle;
					break;
				default:
					print ("\nstate", stateChar, "\n");
					
				}
				
				pi.command = split[15];
				pi.arguments = new String[split.length - 16];
				
				for (int j=0, i=16; i<split.length; j++, i++) {
					pi.arguments[j] = split[i];
				}
				
				
				psLines.add(pi);
			}
			index++;
		}
		
		return psLines;
	}

	
	public static void main (String [] args) {
		print (ps());
	}
	

}
