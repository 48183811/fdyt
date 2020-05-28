package model;

import dto.LandEnclosureFilesDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 宗地权籍信息表
 */
@Entity
@Table(name = "land_rights_info")
public class LandRightsInfo implements Serializable {
    private static final long serialVersionUID = 3210408352049187615L;
    @Id
    @Column(length = 36,nullable = false)
    private String id;        //编号,主键
    @Column(length = 50,nullable = false)
    private String landNumber;        //宗地号
    @Column(length = 50)
    private String householdNumber;        //户号
    @Column(length = 50,nullable = false)
    private String landTrait;        //宗地特征
    @Column(length = 100,nullable = false)
    private String obligee;        //权利人或法人代表
    @Column(length = 4)
    private int population;        //家庭人口数
    @Column(length = 200)
    private String projectName;        //项目名称
    @Column(length = 255)
    private String idCard;        //身份证号码
    @Column(length = 50)
    private String phone;        //联系方式
    @Column(length = 100)
    private String houseNumber ;        //门牌号
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date buildTime;        //建房时间
    @Column(length = 10)
    private String buildingStructure;        //房屋结构
    @Column(length = 4)
    private int floorsTotalNumber;        //房屋总层数
    @Column(length = 200)
    private String licenseNumber;        //批准文号（土地及房屋证书号）
    @Column
    private Double landApprovedArea;        //土地批准面积，平方米
    @Column
    private Double housesApprovedArea;        //房屋批准面积，平方米
    @Column
    private Double landArea;        //占地面积，平方米
    @Column
    private Double equalLandArea;        //平均占地面积（同一宗地存在多户，求平均数），平方米
    @Column
    private Double housesArea;        //建筑面积，平方米
    @Column(length = 200)
    private String housesComplianceCertificate;        //房屋符合规划或建设的证明
    @Column
    private Boolean isVillagers;        //是否本村村名:true-是、false-否
    @Column(length = 100)
    private String obligeeSign;        //户主或法人签字
    @Column(length = 255)
    private String remarks;        //备注
    @Column(length = 50)
    private String town;        //乡镇社区
    @Column(length = 50)
    private String village;        //村
    @Column(length = 50)
    private String villageGroup;        //村民组
    @Column(length = 36)
    private String createrId;    //创建人主键
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;        //创建时间
    @Column(length = 36)
    private String updateId;    //修改人主键
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;        //修改时间
    @Transient
    private List<LandEnclosure> landEnclosureList;  //宗地附件信息列表
    @Transient
    private LandEnclosureFilesDTO landEnclosureFiles;  //宗地附件文件分类列表，用于分类查看附件
    @Transient
    private String coordinates;   //范围坐标串
    @Transient
    private String bufferCoordinates;   //缓冲shp的范围坐标串
    @Transient
    private Float bottomElevation;   //底面高程，米
    @Transient
    private Float buildingHeight;   //建筑高度，米

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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
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

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    public int getFloorsTotalNumber() {
        return floorsTotalNumber;
    }

    public void setFloorsTotalNumber(int floorsTotalNumber) {
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

    public Boolean getVillagers() {
        return isVillagers;
    }

    public void setVillagers(Boolean villagers) {
        isVillagers = villagers;
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

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getBufferCoordinates() {
        return bufferCoordinates;
    }

    public void setBufferCoordinates(String bufferCoordinates) {
        this.bufferCoordinates = bufferCoordinates;
    }

    public Float getBottomElevation() {
        return bottomElevation;
    }

    public void setBottomElevation(Float bottomElevation) {
        this.bottomElevation = bottomElevation;
    }

    public Float getBuildingHeight() {
        return buildingHeight;
    }

    public void setBuildingHeight(Float buildingHeight) {
        this.buildingHeight = buildingHeight;
    }

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }

    public Double getEqualLandArea() {
        return equalLandArea;
    }

    public void setEqualLandArea(Double equalLandArea) {
        this.equalLandArea = equalLandArea;
    }
}
