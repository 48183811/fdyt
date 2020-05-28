package dto;

/***
 * 土规-基本农田DTO
 */
public class TG_JBNT_DTO {
    private Integer gid;        //主键
    private String dlmc;        //规划地类名称
    private String coordinates;   //图斑范围坐标串
    private Double intersectArea; // 图斑与几何相交部分的面积（平方米）

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Double getIntersectArea() {
        return intersectArea;
    }

    public void setIntersectArea(Double intersectArea) {
        this.intersectArea = intersectArea;
    }
}
