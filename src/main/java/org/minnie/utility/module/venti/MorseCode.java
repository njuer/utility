package org.minnie.utility.module.venti;

import java.util.HashMap;
import java.util.Map;

public class MorseCode {
	
//	public static final String CODE_ZERO = "-----";
//	public static final String CODE_ONE = "·----";
//	public static final String CODE_TWO = "··---";
//	public static final String CODE_THREE = "···--";
//	public static final String CODE_FOUR = "····-";
//	public static final String CODE_FIVE = "·····";
//	public static final String CODE_SIX = "-····";
//	public static final String CODE_SEVEN = "--···";
//	public static final String CODE_EIGHT = "---··";
//	public static final String CODE_NINE = "----·";
	
	private final static Map<String,String> MORSE_CODE = new  HashMap<String, String>();

	static{
		MORSE_CODE.put("·----", "1");
		MORSE_CODE.put("··---", "2");
		MORSE_CODE.put("···--", "3");
		MORSE_CODE.put("····-", "4");
		MORSE_CODE.put("·····", "5");
		MORSE_CODE.put("-····", "6");
		MORSE_CODE.put("--···", "7");
		MORSE_CODE.put("---··", "8");
		MORSE_CODE.put("----·", "9");
		MORSE_CODE.put("-----", "0");
	}


	public static void main(String[] args) {
		System.out.println(translate("··--- /····- / ----· / ····- / ····- / ··--- / ··--- / ----· / -···· / ----· / ··--- / -···· / ····- / -···· / ····-​"));
	}
	
	public static String translate(String sentence){
		
		StringBuffer sb = new StringBuffer();
		
		if(null != sentence && !"".equals(sentence)){
			String [] codeArray = sentence.split("/");
			for(String code : codeArray){
				
				code = code.trim();
				String val = MORSE_CODE.get(code);
				if(null != val){
					sb.append(val);
				}
			}
		}
		
		return sb.toString();
	}

}
