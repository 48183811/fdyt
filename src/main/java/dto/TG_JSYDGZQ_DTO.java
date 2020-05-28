package dto;

/***
 * 土规-建设用地管制区DTO
 */
public class TG_JSYDGZQ_DTO {
    private Integer gid;        //主键
    private String gzqlxdm;        //规划地类名称
    private String gzqmj;        //规划地类面积
    private String coordinates;   //图斑范围坐标串
    private Double intersectArea; // 图斑与几何相交部分的面积（平方米）

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGzqlxdm() {
        return gzqlxdm;
    }

    public void setGzqlxdm(String gzqlxdm) {
        this.gzqlxdm = gzqlxdm;
    }

    public String getGzqmj() {
        return gzqmj;
    }

    public void setGzqmj(String gzqmj) {
        this.gzqmj = gzqmj;
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
