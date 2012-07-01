package org.facile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.facile.Facile.*;

import org.facile.ProcessIO.ProcessOut;

public class OS {
	
	
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

	public static List<ProcessInfo> ps () {
		
		                                               //0     1   2   3    4   5   6     7   8-12  13   14   15   16
		ProcessOut pout = run("/bin/sh", "-c", "ps -Ao \"%cpu %mem pid ppid uid gid etime ni lstart pri stat comm args\"");
		String stdout = pout.stdout;
		String[] lines = toLines(stdout);
		
		List<ProcessInfo> psLines = new ArrayList<OS.ProcessInfo>(lines.length);
		
		int index = 0;
		for (String line : lines) {
			if (index!=0) {
				
				String[] split = split(line);
				ProcessInfo pi = new ProcessInfo();
				pi.cpuUsage = toFloat(split[0]);
				pi.memUsage = toFloat(split[1]);
				pi.processId = toInt(split[2]);
				pi.parentProcessId = toInt(split[3]);
				pi.userId = toInt(split[4]);
				pi.groupId = toInt(split[5]);
				print ("elaspedTime", split[6]);
				//String elaspedTime = split[6];
				//pi.elaspedTime = toInt(split[6]);
				pi.nice = toInt(split[7]);
				
				print ("lstart", split[8], split[9], split[10], split[11], split[12]);

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
				pi.arguments = new String[split.length - 15];
				
				for (int j=0, i=15; i<split.length; j++, i++) {
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
