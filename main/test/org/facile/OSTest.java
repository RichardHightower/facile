package org.facile;

import static org.junit.Assert.*;
import static org.facile.Facile.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.facile.OS.ElaspedTime;
import org.facile.OS.FileInfo;
import org.facile.OS.ProcessInfo;
import org.junit.Test;

public class OSTest {

	private static String macOSXPSString = lines(
			"%CPU %MEM   PID  PPID   UID   GID     ELAPSED NI STARTED                      PRI STAT COMM             ARGS",
			"9.0  7.1     3     2     77    66 07-10:27:52  3 Sat Jun 23 11:08:09 2012      31 Rs   /foo/foobar    /bin/goo -l System /foo/foofoo",
			"1.0  0.1    10     1     0     0  07-10:27:22  4 Sat Jun 23 11:08:39 2012      33 Rs   /usr/libexec/kex /usr/libexec/kextd",
			"2.0  0.1    11     1     0     0  07-10:27:22  5 Sat Jun 23 11:08:39 2012      33 Ds   /usr/libexec/Use /usr/libexec/UserEventAgent -l System",
			"3.0  1.1    11     7     33     11  07-10:27:22  6 Sat Jun 23 11:08:39 2012      33 Is   /usr/libexec/Use /usr/libexec/UserEventAgent -l System");

	private static String linuxPSString = lines(
			"%CPU %MEM   PID  PPID   UID   GID     ELAPSED  NI                  STARTED PRI STAT COMMAND         COMMAND",
			"1.1  3.3  1891     1  1000  1000    03:12:28   3 Sat Jun 30 19:14:56 2012  19 Sl   unity-musicstor /usr/lib/uanity-lens-music/unity-music boo bar baz",
			"2.2  1.6  1906     1  1000  1000    03:12:26   0 Sat Jun 30 19:14:58 2012  19 Rl   gnome-terminal  gnome-terminal",
			"3.3  1.0  1911  1906  1000    43    03:12:26   0 Sat Jun 30 19:14:58 2012  19 S    gnome-pty-helpe gnome-pty-helper",
			"4.4  1.1  1912  1906  1000  1000    03:12:26   0 Sat Jun 30 19:14:58 2012  19 Ss   bash            bash",
			"5.5  1.7  1970  1569  1000  1000    03:12:20   0 Sat Jun 30 19:15:04 2012  19 S    applet.py       /usr/bin/python /usr/share/system-con",
			"6.6  1.4  1975  1569  1000  1000    03:11:50   7 Sat Jun 30 19:15:34 2012  19 Dl   update-notifier update-notifier boo bar baz");

	@Test
	public void dateFormat() throws ParseException {

		// Letter Date or Time Component Presentation Examples
		// G Era designator Text AD
		// y Year Year 1996; 96
		// M Month in year Month July; Jul; 07
		// w Week in year Number 27
		// W Week in month Number 2
		// D Day in year Number 189
		// d Day in month Number 10
		// F Day of week in month Number 2
		// E Day in week Text Tuesday; Tue
		// a Am/pm marker Text PM
		// H Hour in day (0-23) Number 0
		// k Hour in day (1-24) Number 24
		// K Hour in am/pm (0-11) Number 0
		// h Hour in am/pm (1-12) Number 12
		// m Minute in hour Number 30
		// s Second in minute Number 55
		// S Millisecond Number 978
		// z Time zone General time zone Pacific Standard Time; PST; GMT-08:00
		// Z Time zone RFC 822 time zone -0800
		// Sat Jun 30 21:20:22 2012

		DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
		Date date = formatter.parse("Sat Jun 30 19:14:56 2012");
		assertEquals("Sat Jun 30 19:14:56 PDT 2012", ""+date);
	}

	@Test
	public void elaspsedTimeTest() {

		ElaspedTime test = new ElaspedTime(77, 88, 33, 44);
		ElaspedTime elaspedTime = OS.elaspedTime("77-88:33:44");
		assertEquals(test, elaspedTime);

		test = new ElaspedTime(0, 22, 33, 44);
		elaspedTime = OS.elaspedTime("22:33:44");
		assertEquals(test, elaspedTime);

		test = new ElaspedTime(0, 0, 33, 44);
		elaspedTime = OS.elaspedTime("33:44");
		assertEquals(test, elaspedTime);

	}

	@Test
	public void testLinux() {
		List<ProcessInfo> out = OS.extractPS(linuxPSString);

		assertEquals(6.6, out.get(5).cpuUsage, 0.1);
		assertEquals(1.4, out.get(5).memUsage, 0.1);
		assertEquals(1975, out.get(5).processId);
		assertEquals(1569, out.get(5).parentProcessId);
		assertEquals(1000, out.get(5).userId);
		assertEquals(1000, out.get(5).groupId);
		assertEquals(19, out.get(5).priority);
		assertEquals(OS.ProcessState.Uninterruptible, out.get(5).state);
		assertEquals(7, out.get(5).nice);
		assertEquals("update-notifier", out.get(5).command);
		assertEquals("update-notifier", out.get(5).arguments[0]);
		assertEquals("boo", out.get(5).arguments[1]);
		assertEquals("bar", out.get(5).arguments[2]);
		assertEquals("baz", out.get(5).arguments[3]);
		assertEquals("Sat Jun 30 19:15:34 PDT 2012", "" + out.get(5).lstart);

		assertEquals(1.1, out.get(0).cpuUsage, 0.1);
		assertEquals(3.3, out.get(0).memUsage, 0.1);
		assertEquals(1891, out.get(0).processId);
		assertEquals(1, out.get(0).parentProcessId);
		assertEquals(1000, out.get(0).userId);
		assertEquals(1000, out.get(0).groupId);
		assertEquals(19, out.get(0).priority);
		assertEquals(OS.ProcessState.Sleep, out.get(0).state);
		assertEquals(3, out.get(0).nice);
		assertEquals("unity-musicstor", out.get(0).command);
		assertEquals("/usr/lib/uanity-lens-music/unity-music",
				out.get(0).arguments[0]);
		assertEquals("boo", out.get(0).arguments[1]);
		assertEquals("bar", out.get(0).arguments[2]);
		assertEquals("baz", out.get(0).arguments[3]);

	}

	@Test
	public void testMacOSX() {
		List<ProcessInfo> out = OS.extractPS(macOSXPSString);
		assertEquals(9.0, out.get(0).cpuUsage, 0.1);
		assertEquals(7.1, out.get(0).memUsage, 0.1);
		assertEquals(3, out.get(0).processId);
		assertEquals(2, out.get(0).parentProcessId);
		assertEquals(77, out.get(0).userId);
		assertEquals(66, out.get(0).groupId);
		assertEquals(31, out.get(0).priority);
		assertEquals(OS.ProcessState.Runnable, out.get(0).state);
		assertEquals(3, out.get(0).nice);
		assertEquals("/foo/foobar", out.get(0).command);
		assertEquals("/bin/goo", out.get(0).arguments[0]);
		assertEquals("-l", out.get(0).arguments[1]);
		assertEquals("/foo/foofoo", out.get(0).arguments[3]);

		assertEquals(3.0, out.get(3).cpuUsage, 0.1);
		assertEquals(1.1, out.get(3).memUsage, 0.1);
		assertEquals(11, out.get(3).processId);
		assertEquals(7, out.get(3).parentProcessId);
		assertEquals(33, out.get(3).userId);
		assertEquals(11, out.get(3).groupId);
		assertEquals(33, out.get(3).priority);
		assertEquals(OS.ProcessState.Idle, out.get(3).state);
		assertEquals(6, out.get(3).nice);
		assertEquals("/usr/libexec/Use", out.get(3).command);
		assertEquals("/usr/libexec/UserEventAgent", out.get(3).arguments[0]);
		assertEquals("-l", out.get(3).arguments[1]);
		assertEquals("System", out.get(3).arguments[2]);

	}
	
	@Test
	public void killSleep() {
		runAsync(0, null, false, "sleep", "1000");
		
		List<ProcessInfo> list = OS.ps();
		
		int pid = -1;
		for (ProcessInfo pi : list){
			if (pi.command.contains("/bin/sleep")) {
				pid = pi.processId;
				print (pid, pi.state, pi.arguments[0]);
				OS.kill(pid);
			}
		}
		

		list = OS.ps();
		pid = -1;
		for (ProcessInfo pi : list){
			if (pi.command.contains("/bin/sleep")) {
				pid = pi.processId;
				print (pid, pi.state, pi.arguments[0]);
			}
		}
		
		assertEquals(-1, pid);
		


	}

	@Test
	public void killSleep2() {
		runAsync(0, null, false, "sleep", "1000");
		
		int id = OS.processSearch("sleep 1000");
		
		assertTrue(id!=-1);
		
		OS.kill(id);

		id = OS.processSearch("sleep");
		
		assertTrue(id==-1);


	}
	
	@Test
	public void lsTest() {
		print (Sys.os());
		List<FileInfo> ls = OS.ls();
		
		for (FileInfo file : ls) {
			print (file);
		}
		
		print (Sys.os());
		ls = OS.recursiveLs();
		
		for (FileInfo file : ls) {
			print (file);
		}

	}

	String lsSample = lines("",
	"lrwxr-xr-x    4 rick  staff      29 Jun 14 14:08:19 2011 webdocs -> /Library/WebServer/Documents/",
	"-rw-rw-r--  3 rick     staff    378 2011-11-29 23:32:44.803207633 -0800 user-data.properties");

	
	@Test 
	public void lsParse() {
		
		List<FileInfo> files = OS.parseLSCommandOutput(lsSample);
		
		assertEquals("Tue Nov 29 23:32:44 PST 2011", ""+files.get(1).date);
		assertEquals("user-data.properties", ""+files.get(1).name);
		assertEquals("rw-rw-r--", ""+files.get(1).permission);
		assertEquals("staff", ""+files.get(1).group);
		assertEquals("rick", ""+files.get(1).user);
		assertEquals("378", ""+files.get(1).size);
		assertEquals("3", ""+files.get(1).numLinks);
		

	}
	

}
