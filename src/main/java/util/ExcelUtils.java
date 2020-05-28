package util;

import constant.Sys;
import enm.RESULT;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import org.springframework.web.multipart.MultipartFile;
import jxl.write.WritableCellFormat;
import jxl.format.Colour;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import java.io.*;
import java.util.List;


/***
 * Excel工具类
 */
public class ExcelUtils {

	/**
	 * 获取Excel单元格样式
	 * @param type 样式类型：tableTopTitle、tableTitle、tableBody
	 * @return
	 */
	public static WritableCellFormat getCellFormat(String type){
		if (type.equals("tableTopTitle")){
			WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 16, WritableFont.BOLD);//定义字体
			try {
				font.setColour(Colour.BLACK);//黑色色字体
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
			WritableCellFormat format = new WritableCellFormat(font);
			try {
				format.setAlignment(jxl.format.Alignment.CENTRE);//左右居中
				format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//上下居中
				format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);//黑色边框
				format.setWrap(true);  //换行
			} catch (WriteException e) {
				e.printStackTrace();
			}
			return format;
		}
		if (type.equals("tableTitle")){
			WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 13, WritableFont.BOLD);//定义字体
			try {
				font.setColour(Colour.BLACK);//黑色色字体
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
			WritableCellFormat format = new WritableCellFormat(font);
			try {
				format.setAlignment(jxl.format.Alignment.CENTRE);//左右居中
				format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//上下居中
				format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);//黑色边框
				format.setWrap(true);  //换行
			} catch (WriteException e) {
				e.printStackTrace();
			}
			return format;
		}
		if (type.equals("tableBody")){
			WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD);//定义字体
			try {
				font.setColour(Colour.BLACK);//黑色色字体
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
			WritableCellFormat format = new WritableCellFormat(font);
			try {
				format.setAlignment(jxl.format.Alignment.CENTRE);//左右居中
				format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//上下居中
				format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);//黑色边框
				format.setWrap(true);  //换行
			} catch (WriteException e) {
				e.printStackTrace();
			}
			return format;
		}

		return null;
	}

}
