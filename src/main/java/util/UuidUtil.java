package util;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 * @author Administrator
 *
 */
public class  UuidUtil {

    /**
     * 生成32位的随机字符
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 生成36位的随机字符
     * @return
     */
    public static String get36UUID() {
        String uuid = UUID.randomUUID().toString().trim();
        return uuid;
    }

    /**
     * 随机生成六位数验证码
     * @return
     */
    public static int getRandomNum(){
        Random r = new Random();
        return r.nextInt(900000)+100000;//(Math.random()*(999999-100000)+100000)
    }
}