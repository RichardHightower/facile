package com.examples;

import static org.facile.Facile.*;
import java.io.File;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unused", "rawtypes"})
public class CreateMarkDownFromJava {
	
	public static void main (String [] args) {
		

		f readLines = new f() { 
			 Map<String, String> f (File file) {
				return mp("name", file.getName(), "contents", readAll(file));
			}
		};
		
		f fileAsLines = new f() { 
			void f (int index, Map<String, String> file) {
				println ("file number %d  named %s has this many %d characters", index, file.get("name"), file.get("contents").length());
			}
		};

		
		List list = map(readLines, filesWithExtension("java"));
		
		enumerate(fileAsLines, list);
	}

}
