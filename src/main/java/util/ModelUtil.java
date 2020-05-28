package util;

import dto.ModelInfoDTO;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ModelUtil {

    /***
     * 获取实体对象的属性名和属性值，只查询具备@Column注解的字段
     * @param o 实体对象
     * @return
     */
    public static dto.ModelInfoDTO getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        ModelInfoDTO modelInfoDTO = new ModelInfoDTO();
        if(fields ==null || fields.length == 0){
            return modelInfoDTO;
        }

        StringBuffer fieldsStr = new StringBuffer();
        StringBuffer valuesStr = new StringBuffer();
        for(int i = 0;i < fields.length ; i++){
            Column annotation = fields[i].getAnnotation(Column.class);
            if(annotation != null){
                String fieldName = fields[i].getName();
                fieldsStr.append(fieldName.toLowerCase()+",");
                String value = getFieldValueByName(fieldName, o);
                if(value != null){
                    valuesStr.append("'"+value+"',");
                }else {
                    valuesStr.append(value+",");
                }
            }
        }
        modelInfoDTO.setFields(fieldsStr.toString().substring(0,fieldsStr.toString().length()-1));
        modelInfoDTO.setValues(valuesStr.toString().substring(0,valuesStr.toString().length()-1));
        return modelInfoDTO;
    }

    /**
     * 根据属性名获取属性值
     * @param fieldName 字段名
     * @param o 实体对象
     * @return
     */
    public static String getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            if(value != null){
                return value.toString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
