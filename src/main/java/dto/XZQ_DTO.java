package dto;

/***
 * 行政区DTO
 */
public class XZQ_DTO {
    private Integer gid;        //主键
    private String xzqdm;        //行政区代码
    private String xzqmc;        //行政区名称
    private String coordinates;   //图斑范围坐标串
    private Double intersectArea; // 图斑与几何相交部分的面积（平方米）

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getXzqdm() {
        return xzqdm;
    }

    public void setXzqdm(String xzqdm) {
        this.xzqdm = xzqdm;
    }

    public String getXzqmc() {
        return xzqmc;
    }

    public void setXzqmc(String xzqmc) {
        this.xzqmc = xzqmc;
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
