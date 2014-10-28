package org.minnie.autocode;

public class NamingRuleConvert {
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String PREFIX = "&#x";

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	public static String toUnicode(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = 2147483647;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);

			if ((aChar > '=') && (aChar < '')) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
				} else {
					outBuffer.append(aChar);
				}
			} else
				switch (aChar) {
				case ' ':
					if ((x == 0) || (escapeSpace))
						outBuffer.append('\\');
					outBuffer.append(' ');
					break;
				case '\t':
					outBuffer.append('\\');
					outBuffer.append('t');
					break;
				case '\n':
					outBuffer.append('\\');
					outBuffer.append('n');
					break;
				case '\r':
					outBuffer.append('\\');
					outBuffer.append('r');
					break;
				case '\f':
					outBuffer.append('\\');
					outBuffer.append('f');
					break;
				case '!':
				case '#':
				case ':':
				case '=':
					outBuffer.append('\\');
					outBuffer.append(aChar);
					break;
				default:
					if ((aChar < ' ') || (aChar > '~')) {
						outBuffer.append('\\');
						outBuffer.append('u');
						outBuffer.append(toHex(aChar >> '\f' & 0xF));
						outBuffer.append(toHex(aChar >> '\b' & 0xF));
						outBuffer.append(toHex(aChar >> '\004' & 0xF));
						outBuffer.append(toHex(aChar & 0xF));
					} else {
						outBuffer.append(aChar);
					}
					break;
				}
		}
		return outBuffer.toString();
	}

	public static String convert(String s) {
		if ((s == null) || ("".equals(s.trim())))
			return null;
		char[] charArray = s.toCharArray();
		int len = charArray.length;
		for (int i = 0; i < len; i++) {
			if ((charArray[i] == '_') && (i != 0) && (i != len - 1)) {
				char afterUnderscores = charArray[(i + 1)];
				String newChar = String.valueOf(afterUnderscores);
				String temp = new StringBuilder().append(String.valueOf('_')).append(String.valueOf(afterUnderscores)).toString();
				s = s.replace(temp, newChar.toUpperCase());
			}
		}
		return s;
	}

	public static String firstLetterToUpperCase(String s) {
		char c = String.valueOf(s.charAt(0)).toUpperCase().charAt(0);
		char[] charArray = s.toCharArray();
		charArray[0] = c;
		return String.valueOf(charArray);
	}

	public static String firstLetterToLowerCase(String s) {
		char c = String.valueOf(s.charAt(0)).toLowerCase().charAt(0);
		char[] charArray = s.toCharArray();
		charArray[0] = c;
		return String.valueOf(charArray);
	}
	
	/**
	 * 替换字符串并让它的下一个字母为大写
	 * 
	 * @param srcStr
	 * @param org
	 * @param ob
	 * @return
	 */
	public static String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
		String newString = "";
		int first = 0;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first) + ob;
				srcStr = srcStr.substring(first + org.length(), srcStr.length());
				srcStr = firstLetterToUpperCase(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}

	public static String native2Ascii(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (i > 0)
				sb.append(";");
			sb.append(char2Ascii(chars[i]));
		}
		sb.append(";");
		return sb.toString();
	}

	private static String char2Ascii(char c) {
		if (c > 'ÿ') {
			StringBuilder sb = new StringBuilder();
			sb.append(PREFIX);
			int code = c >> '\b';
			String tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			code = c & 0xFF;
			tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			return sb.toString();
		}
		return Character.toString(c);
	}

	public static String ascii2Native(String str) {
		String pre = "\\u";
		str = str.replaceAll(PREFIX, "\\\\u");
		str = str.replaceAll(";", "");
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int index = str.indexOf(pre);
		while (index != -1) {
			sb.append(str.substring(begin, index));
			sb.append(ascii2Char(str.substring(index, index + 6)));
			begin = index + 6;
			index = str.indexOf(pre, begin);
		}
		sb.append(str.substring(begin));

		return sb.toString();
	}

	private static char ascii2Char(String str) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}

		if (!"\\u".equals(str.substring(0, 2))) {
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}

		String tmp = str.substring(2, 4);
		int code = Integer.parseInt(tmp, 16) << 8;
		tmp = str.substring(4, 6);
		code += Integer.parseInt(tmp, 16);
		return (char) code;
	}

	public static void main(String[] args) {
		System.out.println(new StringBuilder().append("result5=").append(ascii2Native("&#x589e;&#x52a0;&#x5408;&#x89c4;&#x80a1;&#x7968;")).toString());
		System.out.println(replaceUnderlineAndfirstToUpper("ni_hao_abc","_",""));
	}
}