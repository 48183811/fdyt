package dto;

/***
 * 宗地面积信息分析结果DTO
 */
public class LandAreaAnalyseDTO {
    private String landCount;  //宗地总数
    private String name;       //类别名称
    private String landArea;        //占地面积，平方米
    private String housesArea;        //建筑面积，平方米

    public String getLandCount() {
        return landCount;
    }

    public void setLandCount(String landCount) {
        this.landCount = landCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

    public String getHousesArea() {
        return housesArea;
    }

    public void setHousesArea(String housesArea) {
        this.housesArea = housesArea;
    }
}
