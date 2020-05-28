package dto;

/***
 * 土地现状-地类图斑2009-DTO
 */
public class TDXZ_DLTB2009_DTO {
    private Integer gid;        //主键
    private String dlbm;        //地类编码
    private String dlmc;        //地类名称
    private String qsxzdm;      //权属性质代码
    private String coordinates;   //图斑范围坐标串
    private Double intersectArea; // 图斑与几何相交部分的面积（平方米）


    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getDlbm() {
        return dlbm;
    }

    public void setDlbm(String dlbm) {
        this.dlbm = dlbm;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }

    public String getQsxzdm() {
        return qsxzdm;
    }

    public void setQsxzdm(String qsxzdm) {
        this.qsxzdm = qsxzdm;
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
