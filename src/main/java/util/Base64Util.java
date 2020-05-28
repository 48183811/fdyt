package util;

import constant.Sys;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



import java.io.*;

/***
 * 图片和base64转换工具
 */
public class Base64Util {
    /**
     * 图片转化成base64字符串
     * @param imgPath 图片路径
     * @return
     */
    public static String getImageStr(String imgPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = imgPath;// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        String encode = null; // 返回Base64编码过的字节数组字符串
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 读取图片字节数组
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return encode;
    }

    /**
     * base64字符串转化成图片
     * @param imgData 图片编码
     * @param dir 文件夹路径
     * @param name 文件名
     * @return
     * @throws IOException
     */
    @SuppressWarnings("finally")
    public static boolean generateImage(String imgData, String dir, String name) throws IOException { // 对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {

            File file = new File(dir);
            if(!file.exists()){
                file.mkdirs();
            }

            String path = dir + Sys.BACKSLASH + name;
            out = new FileOutputStream(path);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            return true;
        }
    }
}
