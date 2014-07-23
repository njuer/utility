package org.minnie.utility.module.venti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBoardCode {

	private final static Map<String,String> MAPPING = new  HashMap<String, String>();

	static{
		MAPPING.put("Q", "a");
		MAPPING.put("W", "b");
		MAPPING.put("E", "c");
		MAPPING.put("R", "d");
		MAPPING.put("T", "e");
		MAPPING.put("Y", "f");
		MAPPING.put("U", "g");
		MAPPING.put("I", "h");
		MAPPING.put("O", "i");
		MAPPING.put("P", "j");
		MAPPING.put("A", "k");
		MAPPING.put("S", "l");
		MAPPING.put("D", "m");
		MAPPING.put("F", "n");
		MAPPING.put("G", "o");
		MAPPING.put("H", "p");
		MAPPING.put("J", "q");
		MAPPING.put("K", "r");
		MAPPING.put("L", "s");
		MAPPING.put("Z", "t");
		MAPPING.put("X", "u");
		MAPPING.put("C", "v");
		MAPPING.put("V", "w");
		MAPPING.put("B", "x");
		MAPPING.put("N", "y");
		MAPPING.put("M", "z");
	}
	
	public static void main(String[] args) {
		List<String> list = getAlphabetList("F G G F T S G C T L N G X S O A T D T​");
		StringBuilder sb = new StringBuilder();
		for(String alpha : list){
			sb.append(MAPPING.get(alpha));
		}
		System.out.println(sb.toString());
	}
	
	public static List<String> getAlphabetList(String formula) {
		
		Pattern formulaPattern = Pattern.compile("\\w");
		List<String> list = new ArrayList<String>();
		Matcher matcher = formulaPattern.matcher(formula);
		
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

}
