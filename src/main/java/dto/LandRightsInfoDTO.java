package dto;

import model.LandEnclosure;

import java.util.Date;
import java.util.List;

/***
 * 宗地权籍信息DTO
 */
public class LandRightsInfoDTO {
    private String id;        //编号,主键
    private String landNumber;        //宗地号
    private String landTrait;        //宗地特征
    private String householdNumber;        //户号
    private String obligee;        //权利人或法人代表
    private String population;        //家庭人口数
    private String projectName;        //项目名称
    private String idCard;        //身份证号码
    private String phone;        //联系方式
    private String houseNumber ;        //门牌号
    private Long buildTime;        //建房时间
    private String buildingStructure;        //房屋结构
    private String floorsTotalNumber;        //房屋总层数
    private String licenseNumber;        //批准文号（土地及房屋证书号）
    private Double landApprovedArea;        //土地批准面积，平方米
    private Double housesApprovedArea;        //房屋批准面积，平方米
    private Double landArea;        //占地面积，平方米
    private Double housesArea;        //建筑面积，平方米
    private String housesComplianceCertificate;        //房屋符合规划或建设的证明
    private String isVillagers;        //是否本村村名:true-是、false-否
    private String obligeeSign;        //户主或法人签字
    private String remarks;        //备注
    private String town;        //乡镇社区
    private String village;        //村
    private String villageGroup;        //村民组
    private String createrId;    //创建人主键
    private Long creatTime;        //创建时间
    private String updateId;    //修改人主键
    private Long updateTime;        //修改时间
    private List<LandEnclosure> landEnclosureList;  //宗地附件信息列表
    private LandEnclosureFilesDTO landEnclosureFiles;  //宗地附件文件分类列表，用于分类查看附件

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandNumber() {
        return landNumber;
    }

    public void setLandNumber(String landNumber) {
        this.landNumber = landNumber;
    }

    public String getLandTrait() {
        return landTrait;
    }

    public void setLandTrait(String landTrait) {
        this.landTrait = landTrait;
    }

    public String getObligee() {
        return obligee;
    }

    public void setObligee(String obligee) {
        this.obligee = obligee;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Long buildTime) {
        this.buildTime = buildTime;
    }

    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    public String getFloorsTotalNumber() {
        return floorsTotalNumber;
    }

    public void setFloorsTotalNumber(String floorsTotalNumber) {
        this.floorsTotalNumber = floorsTotalNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Double getLandApprovedArea() {
        return landApprovedArea;
    }

    public void setLandApprovedArea(Double landApprovedArea) {
        this.landApprovedArea = landApprovedArea;
    }

    public Double getHousesApprovedArea() {
        return housesApprovedArea;
    }

    public void setHousesApprovedArea(Double housesApprovedArea) {
        this.housesApprovedArea = housesApprovedArea;
    }

    public String getHousesComplianceCertificate() {
        return housesComplianceCertificate;
    }

    public void setHousesComplianceCertificate(String housesComplianceCertificate) {
        this.housesComplianceCertificate = housesComplianceCertificate;
    }

    public String getIsVillagers() {
        return isVillagers;
    }

    public void setIsVillagers(String isVillagers) {
        this.isVillagers = isVillagers;
    }

    public String getObligeeSign() {
        return obligeeSign;
    }

    public void setObligeeSign(String obligeeSign) {
        this.obligeeSign = obligeeSign;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public List<LandEnclosure> getLandEnclosureList() {
        return landEnclosureList;
    }

    public void setLandEnclosureList(List<LandEnclosure> landEnclosureList) {
        this.landEnclosureList = landEnclosureList;
    }

    public LandEnclosureFilesDTO getLandEnclosureFiles() {
        return landEnclosureFiles;
    }

    public void setLandEnclosureFiles(LandEnclosureFilesDTO landEnclosureFiles) {
        this.landEnclosureFiles = landEnclosureFiles;
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

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }
}
