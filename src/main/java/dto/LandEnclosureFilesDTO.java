package dto;

import model.LandEnclosure;

import java.util.List;

/***
 * 宗地附件分类列表DTO
 */
public class LandEnclosureFilesDTO {
    private List<LandEnclosure> idCardAndOtherInformation;  //1.权利人身份证明及其他材料
    private List<LandEnclosure> scenePicture;               //2.实景照片
    private List<LandEnclosure> householdRegister;          //3.户口本
    private List<LandEnclosure> certificationOfLandSources;  //4.土地来源证明材料
    private List<LandEnclosure> propertyOwnershipCertificate;  //5.房屋合法产权证明（房产证）
    private List<LandEnclosure> demarkationNotification;     //6.指界通知书
    private List<LandEnclosure> houseQuestionnaire;           //7.房屋调查表
    private List<LandEnclosure> cadastreQuestionnaire;       //8.地籍调查表
    private List<LandEnclosure> boundarySitesResultsTable;  //9.界址点成果表
    private List<LandEnclosure> zongMap;                     //10.宗地图
    private List<LandEnclosure> propertyDistributionMap;     //11.房产分户图
    private List<LandEnclosure> realEstateSurveyReport;     //12.不动产测量报告书

    public List<LandEnclosure> getIdCardAndOtherInformation() {
        return idCardAndOtherInformation;
    }

    public void setIdCardAndOtherInformation(List<LandEnclosure> idCardAndOtherInformation) {
        this.idCardAndOtherInformation = idCardAndOtherInformation;
    }

    public List<LandEnclosure> getScenePicture() {
        return scenePicture;
    }

    public void setScenePicture(List<LandEnclosure> scenePicture) {
        this.scenePicture = scenePicture;
    }

    public List<LandEnclosure> getHouseholdRegister() {
        return householdRegister;
    }

    public void setHouseholdRegister(List<LandEnclosure> householdRegister) {
        this.householdRegister = householdRegister;
    }

    public List<LandEnclosure> getCertificationOfLandSources() {
        return certificationOfLandSources;
    }

    public void setCertificationOfLandSources(List<LandEnclosure> certificationOfLandSources) {
        this.certificationOfLandSources = certificationOfLandSources;
    }

    public List<LandEnclosure> getPropertyOwnershipCertificate() {
        return propertyOwnershipCertificate;
    }

    public void setPropertyOwnershipCertificate(List<LandEnclosure> propertyOwnershipCertificate) {
        this.propertyOwnershipCertificate = propertyOwnershipCertificate;
    }

    public List<LandEnclosure> getDemarkationNotification() {
        return demarkationNotification;
    }

    public void setDemarkationNotification(List<LandEnclosure> demarkationNotification) {
        this.demarkationNotification = demarkationNotification;
    }

    public List<LandEnclosure> getHouseQuestionnaire() {
        return houseQuestionnaire;
    }

    public void setHouseQuestionnaire(List<LandEnclosure> houseQuestionnaire) {
        this.houseQuestionnaire = houseQuestionnaire;
    }

    public List<LandEnclosure> getCadastreQuestionnaire() {
        return cadastreQuestionnaire;
    }

    public void setCadastreQuestionnaire(List<LandEnclosure> cadastreQuestionnaire) {
        this.cadastreQuestionnaire = cadastreQuestionnaire;
    }

    public List<LandEnclosure> getBoundarySitesResultsTable() {
        return boundarySitesResultsTable;
    }

    public void setBoundarySitesResultsTable(List<LandEnclosure> boundarySitesResultsTable) {
        this.boundarySitesResultsTable = boundarySitesResultsTable;
    }

    public List<LandEnclosure> getZongMap() {
        return zongMap;
    }

    public void setZongMap(List<LandEnclosure> zongMap) {
        this.zongMap = zongMap;
    }

    public List<LandEnclosure> getPropertyDistributionMap() {
        return propertyDistributionMap;
    }

    public void setPropertyDistributionMap(List<LandEnclosure> propertyDistributionMap) {
        this.propertyDistributionMap = propertyDistributionMap;
    }

    public List<LandEnclosure> getRealEstateSurveyReport() {
        return realEstateSurveyReport;
    }

    public void setRealEstateSurveyReport(List<LandEnclosure> realEstateSurveyReport) {
        this.realEstateSurveyReport = realEstateSurveyReport;
    }
}
