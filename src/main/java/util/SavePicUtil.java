package util;

import constant.Sys;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SavePicUtil {

    /**
     * 返回存储图片路径
     * @param files
     * @param moduleName
     * @param request
     * @return
     */
    public static List<String> savePic(MultipartFile[] files, String moduleName, HttpServletRequest request){
        List<String> list = new ArrayList<>();
        if (null != files && files.length > 0) {
            String ext = null;      //后缀名  如 jpg、png、doc等
            String uuid = null;     //uuid
            String tempName = null; //临时名
            String savePath = null; //保存路径
            File file = null;       // 保存文件
            String webPath = null;    //web相对路径
            FileUtils fu = new FileUtils(); //文件工具类
            Date now = new Date();  //当前日期

            String rootPath = request.getSession().getServletContext().getRealPath("/");

            for (MultipartFile mf : files) {
                tempName = mf.getOriginalFilename();
                if (StringUtil.notEmpty(tempName)) {
                    ext = FileUtils.getExt(tempName);
                    uuid = UUID.randomUUID().toString().replace(Sys.MINUS, Sys.NONE);
                    webPath = Sys.UPLOAD + Sys.BACKSLASH + moduleName + Sys.BACKSLASH + DateUtil.getDay2(now) + Sys.BACKSLASH + uuid + Sys.DOT + ext;
                    savePath = rootPath + webPath;
                    System.out.println("savePath = " + savePath);
                    FileOutputStream fo;

                    file = new File(savePath);
                    fu.createFile(file);
                    try {
                        fo = new FileOutputStream(file);
                        fo.write(mf.getBytes());
                        fo.close();
                        list.add(webPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(list.size() > 0){
            return list;
        }

        return new ArrayList<>();
    }


    /**
     * 返回存储图片路径
     * @param files
     * @param path
     * @param request
     * @return
     */
    public static Boolean saveSnapImages(MultipartFile[] files, String path, HttpServletRequest request){
        boolean save = false;
        if (null != files && files.length > 0) {
            File file = null;       // 保存文件
            FileUtils fu = new FileUtils(); //文件工具类
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            for (MultipartFile mf : files) {
                FileOutputStream fo;
                file = new File(rootPath + Sys.BACKSLASH + path);
                fu.createFile(file);
                try {
                    fo = new FileOutputStream(file);
                    fo.write(mf.getBytes());
                    fo.close();
                    save = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    save = false;
                }
            }
        }
        return save;
    }

}
