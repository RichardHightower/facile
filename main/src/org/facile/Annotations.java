package org.facile;
//import java.lang.annotation.Annotation;
import java.util.Map;

public class Annotations {
	static Map<String, Map<String, ?>> metaData(Class <?> clazz) {
		//Annotation[] classAnnotations = clazz.getAnnotations();
		
		MetaData metaData = new MetaData();
		metaData.className = clazz.getName();
		metaData.clazz = clazz;
		return null;
	}
}
