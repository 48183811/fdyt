package dto;

/***
 * 实体信息
 */
public class ModelInfoDTO {
    private String fields;          //字段名
    private String values;          //字段值

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ModelInfoDTO{" +
                "fields='" + fields + '\'' +
                ", values='" + values + '\'' +
                '}';
    }
}
