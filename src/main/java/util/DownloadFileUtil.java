package util;
import framework.CallResult;
import org.springframework.web.bind.annotation.RequestParam;
import util.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//下载文件工具类
public class DownloadFileUtil {
    /**
     *下载文件
     * @param response
     * @param url   文件路径
     * @return
     * @throws IOException
     */
    public static Boolean downloadFile(HttpServletResponse response,String url) throws IOException{

        //下载
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            //获取文件
            File file = new File(url);
            String fileName=file.getName();
            if(!file.exists()) {
                //如果文件不存在就跳出
                return false;
            }
            ips = new FileInputStream(file);
            //response.setContentType("multipart/form-data");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            //为文件重新设置名字
            response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"),"ISO8859-1") + "\"");
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 4];
            while ((len = ips.read(buffer, 0, buffer.length)) != -1){
                out.write(buffer,0, len);
                out.flush();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (ips != null) {
                    ips.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
