package com.examples;

import static org.facile.Facile.*;

import java.io.File;
import java.util.List;

public class Classes {
	
	static File baseDir = null;
	
	public static String shortFileName(File f) {
		String trim = baseDir.toString();
		String sFile = f.toString();
		return "./" + trimStart(sFile, trim);
	}
	public static void main (String [] args) {
		baseDir = cwf().getParentFile(); ///Users/rick/Documents/workspace/ilovejava/facile on my box
		File mainSrcDir = file(baseDir, "main/src");
		List<String> list = gmap(fn(string, Classes.class, "shortFileName"), files(mainSrcDir, ".*\\.java"));		
		File classes = file(baseDir, "classes");
		writeAll(classes, join(list, " "));
	}

}
