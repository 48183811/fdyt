package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author Administrator
 *
 */
public class StringUtil {

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * @param s 要检测的字符
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * @param s 要检测的字符
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 字符串转换为字符串数组
	 * @param str 字符串
	 * @param splitRegex 分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * @param str 字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/***
	 * 字符串转布尔
	 * @param str
	 * @return
	 */
	public static Boolean stringToBool(String str){
		Boolean result=false;
		try {
			result = Boolean.parseBoolean(str);
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 去除字符串中的 【注：\\n 回车(\u000a)  ，\ \t 水平制表符(\u0009)  ，\\s 空格(\u0008) ，\\r 换行(\u000d)*\/】
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 去除字符串首尾指定的字符
	 * @param str  字符串
	 * @param element  指定的字符
	 * @return
	 */
	public static String removeBeginAndEnd(String str,String element){
		String regex = "^" + element + "*|" + element + "*$";
		return str.replaceAll(regex, "");
	}

	/**
	 * 判定字符串是否为整数(±)
	 * @param str 字符串
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判定字符串是否为数字(含正负小数)
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNumber(String str){
		String reg = "^[-\\+]?[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

}
