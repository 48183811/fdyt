package util;

import constant.Sys;
import enm.RESULT;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;


/***
 * 坐标工具类
 */
public class CoordinatesUtils {

	/**
	 * 判定坐标串是否为polygon
	 * @param coordinates 坐标串(postGIS格式)
	 * @return
	 */
	public static Boolean isPolygon(String coordinates){
		String str = coordinates.toLowerCase();
		return str.startsWith("polygon");
	}

	/**
	 * 判定坐标串是否为MultiPolygon
	 * @param coordinates 坐标串(postGIS格式)
	 * @return
	 */
	public static Boolean isMultiPolygon(String coordinates){
		String str = coordinates.toLowerCase();
		return str.startsWith("multipolygon");
	}

	/**
	 * 将Polygon坐标串转换成MultiPolygon坐标串
	 * @param coordinates 坐标串(postGIS格式)
	 * @return
	 */
	public static String PolygonToMultiPolygon(String coordinates){
		String str = coordinates.toLowerCase();
		String str2 = str.replace("polygon(","MultiPolygon((") + ")";
		return str2;
	}

}
