package org.facile;

import static org.facile.Facile.*;

import java.io.File;
import java.util.List;

public class Sys {

	public static String os() {
		return sprop("os.name");
	}

	public static String osVersion() {
		return sprop("os.version");
	}

	public static List<File> libraryPath() {
		return gmap(file, split(sprop("java.library.path"), ":;"));
	}

	public static List<File> classPath() {
		return gmap(file, split(sprop("java.class.path"), ":;"));
	}

	public static File userHome() {
		return file(sprop("user.home"));
	}

	public static String userName() {
		return sprop("user.name");
	}

	public static String javaSpecVersion() {
		return sprop("java.specification.version");
	}

	public static String[] commandLine() {
		return split(sprop("sun.java.command"));
	}

	public static String lineSeparator() {
		return sprop("line.separator");
	}

	public static File userDir() {
		return file(sprop("user.dir"));
	}

	public static File cwd() {
		return file(sprop("user.dir"));
	}

	public static File javaHome() {
		return file(sprop("java.home"));
	}

	public static File javac() {
		return file(javaHome(), "bin/javac");
	}

	public static File java() {
		return file(javaHome(), "bin/java");
	}

	public static String lang() {
		return sprop("user.language");
	}

	public static String javaVersion() {
		return sprop("java.version");
	}

	public static List<File> extDirs() {
		return gmap(file, split(sprop("java.ext.dirs"), ":;"));
	}

	public static List<File> bootClassPath() {
		return gmap(file, split(sprop("boot.class.path"), ":;"));
	}

	public static List<File> bootLibraryPath() {
		return gmap(file, split(sprop("sun.boot.library.path"), ":;"));
	}

	public static String javaVmVersion() {
		return sprop("java.vm.version");
	}

	public static File shell() {
		return file(System.getenv().get("SHELL"));
	}

	public static List<File> path() {
		return gmap(file, 
				split(System.getenv().get("PATH"), ":;")
				);
	}

}
