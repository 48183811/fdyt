package util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//压缩文件工具类
public class ZipUtil {

    /**
     * 将文件路径列表的所有文件压缩成ZIP
     * @param filePathList 需要压缩文件的路径列表
     * @param out         压缩文件输出流
     * @return  true/false
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static Boolean toZip(List<String> filePathList , OutputStream out)throws RuntimeException {
        final int  BUFFER_SIZE = 2 * 1024;
        ZipOutputStream zos = null ;
        FileInputStream in = null;
        try {
            zos = new ZipOutputStream(out);
            for (String filePath : filePathList) {
                File file = new File(filePath);

                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                in = new FileInputStream(file);
                while ((len = in.read(buf, 0, buf.length)) != -1){
                    zos.write(buf, 0, len);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                if(zos != null){
                    zos.closeEntry();
                    zos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
