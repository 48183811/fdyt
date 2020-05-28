package dto;

/***
 * 根据权利人姓名查询宗地权籍信息DTO
 */
public class LandRightsInfoQueryByObligeeDTO {
    private String id;        //编号,主键
    private String idCard;        //身份证号码
    private String landNumber;        //宗地号
    private String householdNumber;        //户号
    private String obligee;        //权利人或法人代表
    private String phone;        //联系方式
    private String town;        //乡镇社区
    private String village;        //村
    private String villageGroup;        //村民组
    private String bufferCoordinates;   //缓冲shp的范围坐标串
    private Float bottomElevation;   //底面高程，米
    private Float buildingHeight;   //建筑高度，米

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLandNumber() {
        return landNumber;
    }

    public void setLandNumber(String landNumber) {
        this.landNumber = landNumber;
    }

    public String getObligee() {
        return obligee;
    }

    public void setObligee(String obligee) {
        this.obligee = obligee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
