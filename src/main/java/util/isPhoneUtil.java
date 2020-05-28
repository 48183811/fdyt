package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 电话号码校验工具类
  */
public class isPhoneUtil {

    /**
     * 校验手机号
     * @param number  手机号
     * @return
     */
    public static boolean isMoblie(String number){
        Pattern p = Pattern.compile("^[1][3-9]\\d{9}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

//    /**
//     *校验座机号
//     *
//     * 校验规则：
//     *   1、7-8位直拨号码
//     *   2、3-4位区号+7-8位直拨号码+1-4位分机号
//     *   3、3-4位区号+7-8位直拨号码
//     *   4、7-8位直拨号码+1-4位分机号
//     *   注意： 区号、直拨号码、分机号之间要加"-"
//     * @param number  座机号
//     * @return
//     */
//    public static boolean isFixPhome(String number){
//        Pattern p = Pattern.compile("^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$");
//        Matcher m = p.matcher(number);
//        return m.matches();
//    }


    /**
     * 校验座机、手机号
     * 校验规则：
     * 1、11位手机号码
     * 2、3-4位区号+7-8位直拨号码
     * 3、7-8位直拨号码
     *
     *
     * @param phone 座机、手机号
     * @return
     */
    public static boolean isPhone(String phone){
        Pattern p = Pattern.compile("^[1][3-9]\\d{9}$|^((0\\d{2,3}-\\d{7,8})|(\\d{7,8})|(0\\d{2,3}\\d{7,8}))$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
