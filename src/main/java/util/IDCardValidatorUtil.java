package util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * 身份证号码校验工具类
 *
 * 身份证号码的格式：610821-20061222-612-X
 * 由18位数字组成：前6位为地址码，第7至14位为出生日期码，第15至17位为顺序码，
 * 第18位为校验码。检验码分别是0-10共11个数字，当检验码为“10”时，为了保证公民身份证号码18位，所以用“X”表示。虽然校验码为“X”不能更换，但若需全用数字表示，只需将18位公民身份号码转换成15位居民身份证号码，去掉第7至8位和最后1位3个数码。
 * 当今的身份证号码有15位和18位之分。1985年我国实行居民身份证制度，当时签发的身份证号码是15位的，1999年签发的身份证由于年份的扩展（由两位变为四位）和末尾加了效验码，就成了18位。
 * （1）前1、2位数字表示：所在省份的代码；
 * （2）第3、4位数字表示：所在城市的代码；
 * （3）第5、6位数字表示：所在区县的代码；
 * （4）第7~14位数字表示：出生年、月、日；
 * （5）第15、16位数字表示：所在地的派出所的代码；
 * （6）第17位数字表示性别：奇数表示男性，偶数表示女性
 * （7）第18位数字是校检码：根据一定算法生成
 */
public class IDCardValidatorUtil {

    /**
     * 校验码
     */
    private static final String[] validateCodes = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * 因子
     */
    private static final int[] weights = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

    /**
     * 地区编码
     */
    private static final Hashtable zones = new Hashtable();

    /**
     * 初始化省市编码
     */
    static {
        zones.put("11", "北京");
        zones.put("12", "天津");
        zones.put("13", "河北");
        zones.put("14", "山西");
        zones.put("15", "内蒙古");
        zones.put("21", "辽宁");
        zones.put("22", "吉林");
        zones.put("23", "黑龙江");
        zones.put("31", "上海");
        zones.put("32", "江苏");
        zones.put("33", "浙江");
        zones.put("34", "安徽");
        zones.put("35", "福建");
        zones.put("36", "江西");
        zones.put("37", "山东");
        zones.put("41", "河南");
        zones.put("42", "湖北");
        zones.put("43", "湖南");
        zones.put("44", "广东");
        zones.put("45", "广西");
        zones.put("46", "海南");
        zones.put("50", "重庆");
        zones.put("51", "四川");
        zones.put("52", "贵州");
        zones.put("53", "云南");
        zones.put("54", "西藏");
        zones.put("61", "陕西");
        zones.put("62", "甘肃");
        zones.put("63", "青海");
        zones.put("64", "宁夏");
        zones.put("65", "新疆");
        zones.put("71", "台湾");
        zones.put("81", "香港");
        zones.put("82", "澳门");
        zones.put("91", "国外");
    }


    public static void main(String[] args) {
        boolean result = new IDCardValidatorUtil().validate("130204198509042724");
        System.out.println(result);
    }

    /**
     * 身份证验证
     *
     * @param idNumber
     * @return
     */
    public boolean validate(String idNumber) {

        if (Objects.isNull(idNumber)) {
            return false;
        }

        if (Pattern.matches("^[\\d]{15}$", idNumber)) {

            return this.validate15(idNumber);

        } else if (Pattern.matches("^([\\d]{17}((?i)X))|([\\d]{18})$", idNumber)) {

            return this.validate18(idNumber);

        }

        return false;

    }

    /**
     * 验证15位身份证号
     *
     * @param idNumber
     * @return
     */
    private boolean validate15(String idNumber) {
        return this.validate18(this.convert(idNumber));
    }

    /**
     * 验证18位身份证号
     *
     * @param idNumber
     * @return
     */
    private boolean validate18(String idNumber) {
        return (this.checkBirthday(idNumber) && this.checkZone(idNumber) && this.checkValidateCode(idNumber));
    }

    /**
     * 将15位身份证号转成18位身份证号
     *
     * @return
     */
    private String convert(String idNumber) {

        String newIdNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6);

        // 校验码
        int sum = 0;

        for (int i = 0; i < 17; i++) {
            int ai = Integer.parseInt(String.valueOf(newIdNumber.charAt(i)));
            sum = sum + ai * weights[i];
        }

        int modValue = sum % 11;

        String validateCode;

        switch (modValue) {
            case 0:
                validateCode = "1";
                break;
            case 1:
                validateCode = "0";
                break;
            case 2:
                validateCode = "X";
                break;
            default:
                validateCode = "" + (12 - modValue);
        }

        return newIdNumber + validateCode;

    }

    /**
     * 验证出生日期
     *
     * @param idNumber
     * @return
     */
    private boolean checkBirthday(String idNumber) {

        try {
            Date birthday = new SimpleDateFormat("yyyyMMdd").parse(idNumber.substring(6, 14));
            return (System.currentTimeMillis() > birthday.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 验证地区
     *
     * @param idNumber
     * @return
     */
    private boolean checkZone(String idNumber) {
        return zones.containsKey(idNumber.substring(0, 2));
    }

    /**
     * 验证末位校验码
     * 判断第18位校验码是否正确
     * 第18位校验码的计算方式：
     * 1. 对前17位数字本体码加权求和
     * 公式为：S = Sum(Ai * Wi), i = 0, ... , 16
     * 其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2. 用11对计算结果取模
     * Y = mod(S, 11)
     * 3. 根据模的值得到对应的校验码
     * 对应关系为：
     * Y值：     0  1  2  3  4  5  6  7  8  9  10
     * 校验码： 1  0  X  9  8  7  6  5  4  3   2
     *
     * @param idNumber
     * @return
     */
    private boolean checkValidateCode(String idNumber) {

        int sum = 0;

        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * weights[i];
        }

        int modValue = sum % 11;

        String validateCode = validateCodes[modValue];

        return idNumber.substring(idNumber.length() - 1).equalsIgnoreCase(validateCode);

    }


}
