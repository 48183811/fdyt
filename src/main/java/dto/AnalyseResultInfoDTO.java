package dto;

/***
 * 分析结果信息DTO
 */
public class AnalyseResultInfoDTO {
    private String name;        //地类名称
    private String area;        //总面积，平方米

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
