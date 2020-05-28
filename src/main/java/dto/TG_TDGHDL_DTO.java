package dto;

import model.LandEnclosure;

import java.util.List;

/***
 * 土规-土地规划地类DTO
 */
public class TG_TDGHDL_DTO {
    private Integer gid;        //主键
    private String ghdlmc;        //规划地类名称
    private String ghdlmj;        //规划地类面积
    private String coordinates;   //图斑范围坐标串
    private Double intersectArea; // 图斑与几何相交部分的面积（平方米）
    private String householdNumber;        //户号（不动产单元号）
    private String obligee;        //权利人或法人代表

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGhdlmc() {
        return ghdlmc;
    }

    public void setGhdlmc(String ghdlmc) {
        this.ghdlmc = ghdlmc;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getGhdlmj() {
        return ghdlmj;
    }

    public void setGhdlmj(String ghdlmj) {
        this.ghdlmj = ghdlmj;
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
}
