package dto;

/***
 * 宗地建成时间统计DTO
 */
public class LandBuildTimeCountDTO {
    private String name;       //类型名称
    private int landCount;  //宗地总数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLandCount() {
        return landCount;
    }

    public void setLandCount(int landCount) {
        this.landCount = landCount;
    }
}
