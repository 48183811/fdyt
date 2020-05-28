package util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//数据校验工具类
public class DataTransformUtil {


    /**
     * 校验宗地附件的文件类型
     * @param fileType 文件类型
     * @return 文件类型的类别序号，当不存在/错误时返回“-1”
     */
    public static String checkLandEnclosureFileType(String fileType){
        if (StringUtil.isEmpty(fileType)){
            return "-1";
        }
        if (fileType.equals("权利人身份证明及其他材料")){
            return "1";
        }
        if (fileType.equals("实景照片")){
            return "2";
        }
        if (fileType.equals("户口本")){
            return "3";
        }
        if (fileType.equals("土地来源证明材料")){
            return "4";
        }
        if (fileType.equals("房屋合法产权证明（房产证）") || fileType.equals("房屋合法产权证明(房产证)")){
            return "5";
        }
        if (fileType.equals("指界通知书")){
            return "6";
        }
        if (fileType.equals("房屋调查表")){
            return "7";
        }
        if (fileType.equals("地籍调查表")){
            return "8";
        }
        if (fileType.equals("界址点成果表")){
            return "9";
        }
        if (fileType.equals("宗地图")){
            return "10";
        }
        if (fileType.equals("房产分户图")){
            return "11";
        }
        if (fileType.equals("不动产测量报告书")){
            return "12";
        }
        return "-1";
    }


    /**
     * 将土规_建设用地管制区代码转换成名称
     * @param code 建设用地管制区代码
     * @return 建设用地管制区名称
     */
    public static String jconstructionLandControlArea_transform(String code){
        String str = StringUtil.removeBeginAndEnd(code," ");
        if (str.equals("010")){
            return "允许建设区";
        }
        if (str.equals("020")){
            return "有条件建设区";
        }
        if (str.equals("030")){
            return "限制建设区";
        }
        if (str.equals("040")){
            return "禁止建设区";
        }
        return str;
    }

}
