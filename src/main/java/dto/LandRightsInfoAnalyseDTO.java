package dto;

import model.LandEnclosure;

import java.util.List;

/***
 * 宗地权籍分析DTO
 */
public class LandRightsInfoAnalyseDTO {
    private String id;        //宗地权籍信息主键
    private Integer gid;      //房地一体shp数据主键
    private String landNumber;  //宗地号
    private String householdNumber;        //户号
    private String obligee;        //权利人或法人代表
    private String buildingStructure;        //房屋结构
    private Double landArea;        //占地面积，平方米
    private Double housesArea;        //建筑面积，平方米
    private Boolean isVillagers;        //是否本村村名:true-是、false-否
    private String town;        //乡镇社区
    private String village;        //村
    private String villageGroup;        //村民组
    private String coordinates;        //图斑范围坐标串

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getLandNumber() {
        return landNumber;
    }

    public void setLandNumber(String landNumber) {
        this.landNumber = landNumber;
    }

    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    public Double getLandArea() {
        return landArea;
    }

    public void setLandArea(Double landArea) {
        this.landArea = landArea;
    }

    public Double getHousesArea() {
        return housesArea;
    }

    public void setHousesArea(Double housesArea) {
        this.housesArea = housesArea;
    }

    public Boolean getVillagers() {
        return isVillagers;
    }

    public void setVillagers(Boolean villagers) {
        isVillagers = villagers;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillageGroup() {
        return villageGroup;
    }

    public void setVillageGroup(String villageGroup) {
        this.villageGroup = villageGroup;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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
