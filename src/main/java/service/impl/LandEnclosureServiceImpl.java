package service.impl;


import constant.Sys;
import dao.LandEnclosureDAO;
import enm.RESULT;
import model.LandEnclosure;
import model.LandRightsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.ILandEnclosureService;
import util.FileUtils;
import util.StringUtil;
import util.UuidUtil;

import java.util.Date;
import java.util.List;


@Service
public class LandEnclosureServiceImpl implements ILandEnclosureService {
    @Autowired
    LandEnclosureDAO landEnclosureDAO;

    @Override
    public LandEnclosure get(String id) {
        if (StringUtil.isEmpty(id)){
            return null;
        }
        return landEnclosureDAO.get(id);
    }

    @Override
    public String saveFile(String fileType, MultipartFile file,LandRightsInfo landRightsInfo) {
        //获取文件后缀
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        String town = landRightsInfo.getTown();
        String village = landRightsInfo.getVillage();
        String villageGroup = landRightsInfo.getVillageGroup();
        String landNumber = landRightsInfo.getLandNumber();
        String obligee = landRightsInfo.getObligee();
        String uuid = UuidUtil.get36UUID();
        //合成相对路径
        String webPath = Sys.UPLOAD + Sys.BACKSLASH + "landEnclosure" + Sys.BACKSLASH + town + Sys.BACKSLASH + village + Sys.BACKSLASH + villageGroup + Sys.BACKSLASH + landNumber + Sys.UNDERLINE + obligee + Sys.BACKSLASH + fileType + Sys.BACKSLASH + uuid + Sys.DOT + ext;
        //获取文件保存路径
        String savePath = FileUtils.getSavePath(webPath);
        //保存文件
        RESULT saveFilse = FileUtils.saveFile(file,savePath);
        if (saveFilse == RESULT.SUCCESS){
            return webPath;
        }
        return null;
    }

    @Override
    public RESULT saveLandEnclosure(String householdNumber,String webPath, String userId, Date presentTime,MultipartFile file,LandEnclosure landEnclosure) {
        landEnclosure.setId(UuidUtil.get36UUID());
        landEnclosure.setHouseholdNumber(householdNumber);
        landEnclosure.setFilePath(webPath);
        if (StringUtil.isEmpty(landEnclosure.getFileType())){
            return RESULT.PARAMETER_ERROR;
        }
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        landEnclosure.setSuffix(ext);
        String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        if (fileName.length() > 500){
            fileName = fileName.substring(0,500);
        }
        landEnclosure.setFileName(fileName);
        landEnclosure.setCreaterId(userId);
        landEnclosure.setCreatTime(presentTime);
        landEnclosure.setUpdateId(null);
        landEnclosure.setUpdateTime(null);
        return landEnclosureDAO.add(landEnclosure);
    }

    @Override
    public RESULT delLandEnclosure(String id) {
        if (StringUtil.isEmpty(id)){
            return RESULT.PARAMETER_EMPTY;
        }
        return landEnclosureDAO.del(id);
    }

    @Override
    public RESULT updLandEnclosure(String userId, String id, Date presentTime, String webPath,MultipartFile file) {
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(id) || StringUtil.isEmpty(webPath) || presentTime == null){
            return RESULT.PARAMETER_EMPTY;
        }
        LandEnclosure landEnclosure = landEnclosureDAO.get(id);
        if (landEnclosure == null){
            return RESULT.PARAMETER_ERROR;
        }
        landEnclosure.setFilePath(webPath);
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        landEnclosure.setSuffix(ext);
        String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
        if (fileName.length() > 500){
            fileName = fileName.substring(0,500);
        }
        landEnclosure.setFileName(fileName);
        landEnclosure.setUpdateId(userId);
        landEnclosure.setUpdateTime(presentTime);
        return landEnclosureDAO.upd(landEnclosure);
    }

    @Override
    public List<LandEnclosure> findByHouseholdNumber(String householdNumber) {
        if (StringUtil.isEmpty(householdNumber)){
            return null;
        }
        return landEnclosureDAO.findByHouseholdNumber(householdNumber);
    }


    /****************************************私有方法********************************************/

}
