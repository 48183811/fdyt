package dto;

/***
 * 规划信息下载DTO
 */
public class PlanningInfoDownload_DTO {
    private Integer gid;        //规划地类主键
    private String dlmc;        //规划地类名称
    private String dlmj;        //规划地类面积
    private Double intersectArea; // 规划图斑与几何相交部分的面积（平方米）
    private String landNumber;        //宗地号
    private String householdNumber;        //户号（不动产单元号）
    private String obligee;        //权利人或法人代表

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

    public String getDlmj() {
        return dlmj;
    }

    public void setDlmj(String dlmj) {
        this.dlmj = dlmj;
    }

    public Double getIntersectArea() {
        return intersectArea;
    }

    public void setIntersectArea(Double intersectArea) {
        this.intersectArea = intersectArea;
    }

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }

    public String getObligee() {
        return obligee;
    }

    public void setObligee(String obligee) {
        this.obligee = obligee;
    }

    public String getLandNumber() {
        return landNumber;
    }

    public void setLandNumber(String landNumber) {
        this.landNumber = landNumber;
    }
}
