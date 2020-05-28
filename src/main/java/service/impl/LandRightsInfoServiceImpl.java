package service.impl;


import dao.LandEnclosureDAO;
import dao.LandRightsInfoDAO;
import dto.LandBuildTimeCountDTO;
import dto.LandEnclosureFilesDTO;
import dto.LandRightsInfoDTO;
import enm.RESULT;
import framework.PageBean;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import model.LandEnclosure;
import model.LandRightsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ILandRightsInfoService;
import util.ExcelUtils;
import util.StringUtil;
import util.UuidUtil;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class LandRightsInfoServiceImpl implements ILandRightsInfoService {
    @Autowired
    LandRightsInfoDAO landRightsInfoDAO;
    @Autowired
    LandEnclosureDAO landEnclosureDAO;

    @Override
    public RESULT queryByObligee(String obligee, PageBean pb) {
        return landRightsInfoDAO.queryByObligee(obligee,pb);
    }

    @Override
    public LandRightsInfo getByLandNumber(String landNumber) {
        if (StringUtil.isEmpty(landNumber)){
            return null;
        }
        //查询宗地权籍信息
        LandRightsInfo landRightsInfo = landRightsInfoDAO.getByLandNumber(landNumber);
        if (landRightsInfo == null){
            return null;
        }
        //查询宗地附件
        List<LandEnclosure>landEnclosureList = landEnclosureDAO.findByLandNumber(landNumber);
        if (landEnclosureList != null && landEnclosureList.size() > 0){
            //设置宗地附件文件分类列表
            landRightsInfo.setLandEnclosureFiles(handleLandEnclosureList(landEnclosureList));
        }
        return landRightsInfo;
    }

    @Override
    public LandRightsInfo getByHouseholdNumber(String householdNumber) {
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }
        //查询宗地权籍信息
        LandRightsInfo landRightsInfo = landRightsInfoDAO.getByHouseholdNumber(householdNumber);
        if (landRightsInfo == null){
            return null;
        }
        //查询宗地附件
        List<LandEnclosure>landEnclosureList = landEnclosureDAO.findByHouseholdNumber(householdNumber);
        if (landEnclosureList != null && landEnclosureList.size() > 0){
            //设置宗地附件文件分类列表
            landRightsInfo.setLandEnclosureFiles(handleLandEnclosureList(landEnclosureList));
        }
        return landRightsInfo;
    }

    @Override
    public LandRightsInfo save(LandRightsInfoDTO landRightsInfoDTO, String userId, Date presentTime) {
        LandRightsInfo landRightsInfo = setLandRightsInfo("add",new LandRightsInfo(), landRightsInfoDTO,userId,presentTime);
        if (landRightsInfoDAO.add(landRightsInfo) == RESULT.SUCCESS){
            return landRightsInfo;
        }
        return null;
    }

    @Override
    public LandRightsInfo upd(LandRightsInfoDTO landRightsInfoDTO, String userId, Date presentTime) {
        LandRightsInfo landRightsInfoDb = setLandRightsInfo("upd",landRightsInfoDAO.get(landRightsInfoDTO.getId()), landRightsInfoDTO,userId,presentTime);
        if (landRightsInfoDAO.upd(landRightsInfoDb) == RESULT.SUCCESS){
            return landRightsInfoDb;
        }
        return null;
    }

    @Override
    public RESULT queryByCoordinates(String type,String coordinates,Float maxLandArea,Float maxHousesArea,PageBean pb) {
        if (StringUtil.isEmpty(coordinates)){
            return RESULT.PARAMETER_EMPTY;
        }
        return landRightsInfoDAO.queryByCoordinates(type,coordinates,maxLandArea,maxHousesArea,pb);
    }

    @Override
    public List<LandRightsInfo> queryAllOverproof(String type, String coordinates, Float maxLandArea, Float maxHousesArea) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return landRightsInfoDAO.queryAllOverproof(type,coordinates,maxLandArea,maxHousesArea);
    }

    @Override
    public Boolean downloadPlanningInfo(String type, List<LandRightsInfo> landRightsInfoList, OutputStream os) {
        String sheetName = "";  //Excel工作表名称
        if (type.equals("land")){
            sheetName = "占地面积超标";
        }
        if (type.equals("houses")){
            sheetName = "建筑面积超标";
        }
        if (type.equals("landAndHouses")){
            sheetName = "占地面积和建筑面积均超标";
        }
        if (type.equals("landOrHouses")){
            sheetName = "占地面积或建筑面积超标";
        }

        //创建excel输出流
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            //创建工作表
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            Label label = null;
            sheet.mergeCells(0,0,4,0);
            //添加表格标题
            label = new Label(0,0,"房地一体面积（" + sheetName + "）统计一览表", ExcelUtils.getCellFormat("tableTopTitle"));
            sheet.addCell(label);
            //添加表格行标题
            label = new Label(0,1,"宗地号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(1,1,"不动产单元号",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(2,1,"权利人",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(3,1,"占地面积（平方米）",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);
            label = new Label(4,1,"建筑面积（平方米）",ExcelUtils.getCellFormat("tableTitle"));
            sheet.addCell(label);

            String landNumber = null;         //宗地号
            String householdNumber  = null;        //户号（不动产单元号）
            String obligee  = null;        //权利人或法人代表
            Double landArea = 0.0;          //占地面积，平方米
            Double housesArea = 0.0;        //建筑面积，平方米

            int mergeLandRowNum = 0;    //当前宗地的合并单元格行数。

            for (int i = 0;i < landRightsInfoList.size();i++){
                LandRightsInfo landRightsInfo = landRightsInfoList.get(i);
                if (i == 0){
                    landNumber = StringUtil.replaceBlank(landRightsInfo.getLandNumber());
                    householdNumber = StringUtil.replaceBlank(landRightsInfo.getHouseholdNumber());
                    obligee = landRightsInfo.getObligee();
                    landArea = landRightsInfo.getLandArea();
                    housesArea = landRightsInfo.getHousesArea();
                    if (landRightsInfoList.size() == 1){
                        //数据列表只有一条记录，写出Excel
                        label = new Label(0,i+1,landNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(1,i+1,householdNumber,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(2,i+1,obligee,ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(3,i+1,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                        label = new Label(4,i+1,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                        sheet.addCell(label);
                    }
                    continue;
                }

                //写出Excel
                label = new Label(0,i+1,landNumber,ExcelUtils.getCellFormat("tableBody"));
                sheet.addCell(label);
                label = new Label(1,i+1,householdNumber,ExcelUtils.getCellFormat("tableBody"));
                sheet.addCell(label);
                label = new Label(2,i+1,obligee,ExcelUtils.getCellFormat("tableBody"));
                sheet.addCell(label);
                label = new Label(3,i+1,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                sheet.addCell(label);
                label = new Label(4,i+1,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                sheet.addCell(label);
                if (landNumber.equals(StringUtil.replaceBlank(landRightsInfo.getLandNumber()))){
                    //宗地号相同
                    mergeLandRowNum += 1;
                }else {
                    //宗地号不同
                    if (mergeLandRowNum > 0){
                        //合并单元格
                        int startRow = i-mergeLandRowNum+1;
                        int endRow = i+1;
                        sheet.mergeCells(0,startRow,0,endRow);   //户号
                    }
                    mergeLandRowNum = 0;
                }

                //重置参数
                landNumber = StringUtil.replaceBlank(landRightsInfo.getLandNumber());
                householdNumber = StringUtil.replaceBlank(landRightsInfo.getHouseholdNumber());
                obligee = landRightsInfo.getObligee();
                landArea = landRightsInfo.getLandArea();
                housesArea = landRightsInfo.getHousesArea();

                if (i == (landRightsInfoList.size() - 1)){
                    //列表循环完毕，写出Excel
                    label = new Label(0,i+2,landNumber,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(1,i+2,householdNumber,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(2,i+2,obligee,ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(3,i+2,String.format("%.2f",landArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    label = new Label(4,i+2,String.format("%.2f",housesArea),ExcelUtils.getCellFormat("tableBody"));
                    sheet.addCell(label);
                    if (mergeLandRowNum > 0){
                        //合并单元格
                        int startRow = i-mergeLandRowNum+2;
                        int endRow = i+2;
                        sheet.mergeCells(0,startRow,0,endRow);   //户号
                    }
                }
            }

            //设置列宽
            sheet.setColumnView(0,30);
            sheet.setColumnView(1,40);
            sheet.setColumnView(2,30);
            sheet.setColumnView(3,30);
            sheet.setColumnView(4,30);

            workbook.write();
            workbook.close();
            os.flush();
            os.close();
        }catch  (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<LandBuildTimeCountDTO> queryLandTotalByBuildTime(String type, String coordinates, Float maxLandArea, Float maxHousesArea, Date buildTime) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(coordinates)){
            return new ArrayList<>();
        }
        return landRightsInfoDAO.queryLandTotalByBuildTime(type,coordinates,maxLandArea,maxHousesArea,buildTime);
    }


    /****************************************私有方法********************************************/
    /**
     * 处理宗地附件分类列表
     * @param landEnclosureList 宗地附件列表
     * @return
     */
    private LandEnclosureFilesDTO handleLandEnclosureList(List<LandEnclosure> landEnclosureList){
        if (landEnclosureList == null || landEnclosureList.size() == 0){
            return null;
        }
        List<LandEnclosure> idCardAndOtherInformation = new ArrayList<>();  //1.权利人身份证明及其他材料
        List<LandEnclosure> scenePicture = new ArrayList<>();               //2.实景照片
        List<LandEnclosure> householdRegister = new ArrayList<>();          //3.户口本
        List<LandEnclosure> certificationOfLandSources = new ArrayList<>();  //4.土地来源证明材料
        List<LandEnclosure> propertyOwnershipCertificate = new ArrayList<>();  //5.房屋合法产权证明（房产证）
        List<LandEnclosure> demarkationNotification = new ArrayList<>();     //6.指界通知书
        List<LandEnclosure> houseQuestionnaire = new ArrayList<>();           //7.房屋调查表
        List<LandEnclosure> cadastreQuestionnaire = new ArrayList<>();       //8.地籍调查表
        List<LandEnclosure> boundarySitesResultsTable = new ArrayList<>();  //9.界址点成果表
        List<LandEnclosure> zongMap = new ArrayList<>();                     //10.宗地图
        List<LandEnclosure> propertyDistributionMap = new ArrayList<>();     //11.房产分户图
        List<LandEnclosure> realEstateSurveyReport = new ArrayList<>();     //12.不动产测量报告书
        //宗地附件分类
        for (LandEnclosure landEnclosure:landEnclosureList){
            String fileType = landEnclosure.getFileType();
            if (fileType.equals("权利人身份证明及其他材料")){
                idCardAndOtherInformation.add(landEnclosure);
                continue;
            }
            if (fileType.equals("实景照片")){
                scenePicture.add(landEnclosure);
                continue;
            }
            if (fileType.equals("户口本")){
                householdRegister.add(landEnclosure);
                continue;
            }
            if (fileType.equals("土地来源证明材料")){
                certificationOfLandSources.add(landEnclosure);
                continue;
            }
            if (fileType.equals("房屋合法产权证明（房产证）") || fileType.equals("房屋合法产权证明(房产证)")){
                propertyOwnershipCertificate.add(landEnclosure);
                continue;
            }
            if (fileType.equals("指界通知书")){
                demarkationNotification.add(landEnclosure);
                continue;
            }
            if (fileType.equals("房屋调查表")){
                houseQuestionnaire.add(landEnclosure);
                continue;
            }
            if (fileType.equals("地籍调查表")){
                cadastreQuestionnaire.add(landEnclosure);
                continue;
            }
            if (fileType.equals("界址点成果表")){
                boundarySitesResultsTable.add(landEnclosure);
                continue;
            }
            if (fileType.equals("宗地图")){
                zongMap.add(landEnclosure);
                continue;
            }
            if (fileType.equals("房产分户图")){
                propertyDistributionMap.add(landEnclosure);
                continue;
            }
            if (fileType.equals("不动产测量报告书")){
                realEstateSurveyReport.add(landEnclosure);
            }
        }
        //设置宗地附件分类列表
        LandEnclosureFilesDTO landEnclosureFilesDTO = new LandEnclosureFilesDTO();
        landEnclosureFilesDTO.setIdCardAndOtherInformation(idCardAndOtherInformation);
        landEnclosureFilesDTO.setScenePicture(scenePicture);
        landEnclosureFilesDTO.setHouseholdRegister(householdRegister);
        landEnclosureFilesDTO.setCertificationOfLandSources(certificationOfLandSources);
        landEnclosureFilesDTO.setPropertyOwnershipCertificate(propertyOwnershipCertificate);
        landEnclosureFilesDTO.setDemarkationNotification(demarkationNotification);
        landEnclosureFilesDTO.setHouseQuestionnaire(houseQuestionnaire);
        landEnclosureFilesDTO.setCadastreQuestionnaire(cadastreQuestionnaire);
        landEnclosureFilesDTO.setBoundarySitesResultsTable(boundarySitesResultsTable);
        landEnclosureFilesDTO.setZongMap(zongMap);
        landEnclosureFilesDTO.setPropertyDistributionMap(propertyDistributionMap);
        landEnclosureFilesDTO.setRealEstateSurveyReport(realEstateSurveyReport);
        return landEnclosureFilesDTO;
    }

    /**
     * 设置宗地权籍信息（保存、更新）
     * @param type add/upd
     * @param landRightsInfo 宗地权籍信息
     * @param landRightsInfoDTO 宗地权籍信息DTO
     * @param userId 用户主键
     * @param presentTime 当前系统时间
     * @return
     */
    private LandRightsInfo setLandRightsInfo(String type, LandRightsInfo landRightsInfo, LandRightsInfoDTO landRightsInfoDTO, String userId, Date presentTime){
        if (type.equals("add")){
            landRightsInfo.setId(UuidUtil.get36UUID());
            landRightsInfo.setCreaterId(userId);
            landRightsInfo.setCreatTime(presentTime);
            landRightsInfo.setUpdateId(null);
            landRightsInfo.setUpdateTime(null);
        }else{
            landRightsInfo.setUpdateId(userId);
            landRightsInfo.setUpdateTime(presentTime);
        }
        landRightsInfo.setLandNumber(landRightsInfoDTO.getLandNumber());
        landRightsInfo.setHouseholdNumber(landRightsInfoDTO.getHouseholdNumber());
        landRightsInfo.setLandTrait(landRightsInfoDTO.getLandTrait());
        landRightsInfo.setObligee(landRightsInfoDTO.getObligee());
        landRightsInfo.setPopulation(Integer.parseInt(landRightsInfoDTO.getPopulation()));
        landRightsInfo.setProjectName(landRightsInfoDTO.getProjectName());
        landRightsInfo.setIdCard(landRightsInfoDTO.getIdCard());
        landRightsInfo.setPhone(landRightsInfoDTO.getPhone());
        landRightsInfo.setHouseNumber(landRightsInfoDTO.getHouseNumber());
        landRightsInfo.setBuildTime(new Date(landRightsInfoDTO.getBuildTime()));
        landRightsInfo.setBuildingStructure(landRightsInfoDTO.getBuildingStructure());
        landRightsInfo.setFloorsTotalNumber(Integer.parseInt(landRightsInfoDTO.getFloorsTotalNumber()));
        landRightsInfo.setLicenseNumber(landRightsInfoDTO.getLicenseNumber());
        landRightsInfo.setLandApprovedArea(landRightsInfoDTO.getLandApprovedArea());
        landRightsInfo.setHousesApprovedArea(landRightsInfoDTO.getHousesApprovedArea());
        landRightsInfo.setLandArea(landRightsInfoDTO.getLandArea());
        landRightsInfo.setHousesArea(landRightsInfoDTO.getHousesArea());
        landRightsInfo.setHousesComplianceCertificate(landRightsInfoDTO.getHousesComplianceCertificate());
        if (landRightsInfoDTO.getIsVillagers().equals("true")){
            landRightsInfo.setVillagers(true);
        }else {
            landRightsInfo.setVillagers(false);
        }
        landRightsInfo.setObligeeSign(landRightsInfoDTO.getObligeeSign());
        landRightsInfo.setRemarks(landRightsInfoDTO.getRemarks());
        landRightsInfo.setTown(landRightsInfoDTO.getTown());
        landRightsInfo.setVillage(landRightsInfoDTO.getVillage());
        landRightsInfo.setVillageGroup(landRightsInfoDTO.getVillageGroup());
        return landRightsInfo;
    }


}
