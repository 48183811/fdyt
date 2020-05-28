package util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ly on 2019/7/25.
 */
public class ResponseMapUtil {
    /**
     * 用户名密码错误
     */
    public final static int USER_PASSWORD_FAILURE = 100;
    /**
     * 传输数据为空
     */
    public final static int JSON_NULL = 101;
    /**
     * 数据转换错误
     */
    public final static int DATA_TRANSFORM_ERROR =102;
    /**
     * 验证码错误
     */
    public final static int AUTH_CODE_ERROR =103;
    /**
     * 用户名已经存在
     */
    public final static int USER_ALREADY_EXIST =104;
    /**
     * HQL执行错误
     */
    public final static int HQL_CALCULATE_ERROR =105;
    /**
     * 数据保存失败
     */
    public final static int DATA_SAVE_ERROR=106;

    /**
     * 数据库查询结果为空
     */
    public final static int HQL_RESULTS_NULL=107;

    public static Map<String,Object> errorMap (int errorType){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("flag",false);
        map.put("data",errorType);
        map.put("error",declaredFieldName(errorType));
        return map;
    }
    public static Map<String,Object> errorMap (int errorType,String errorText){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("flag",false);
        map.put("data",errorType);
        map.put("error",errorText);
        return map;
    }
    private static String declaredFieldName(int num){
        try {
            for(Field f : ResponseMapUtil.class.getDeclaredFields()){
                int aaa = f.getInt(ResponseMapUtil.class);
                if(aaa == num){
                    return f.getName();
                }

            }
        }
        catch (Exception e){
            return "";
        }
        return "";
    }

    public  static Map<String,Object> successMap(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("flag",true);
        map.put("data","success");
        return map;
    }
    public  static Map<String,Object> successMap(String successText){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("flag",true);
        map.put("data",successText);
        return map;
    }
}
