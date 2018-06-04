package com.blog.mango.module;

import org.springframework.stereotype.Component;

@Component
public class PrintWriterUtil {
	public static String inScript(String html) {
		return "<script>" + html + "</script>"; 
	}
	
	public static String inAlert(String text) {
		return "alert('" + text + "');";
	}
	
	public static String goPath(String path) {
		return "location.href= '"+ path + "';";
	}
	
	public static String alertScript(String text) {
		return inScript(inAlert(text));
	}
	
	public static String goScript(String text, String path) {
		return inScript(inAlert(text) + goPath(path));
	}
}
