package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 宗地附件记录表
 */
@Entity
@Table(name = "land_enclosure")
public class LandEnclosure implements Serializable {
    private static final long serialVersionUID = 3690541875819992721L;
    @Id
    @Column(length = 36,nullable = false)
    private String id;        //编号,主键
    @Column(length = 50,nullable = false)
    private String landNumber;        //宗地号
    @Column(length = 50)
    private String householdNumber;        //户号
    @Column(length = 1000,nullable = false)
    private String filePath;        //文件相对路径
    @Column(length = 20,nullable = false)
    private String fileType;        //文件类型
    @Column(length = 500)
    private String fileName;        //文件名称
    @Column(length = 10)
    private String suffix;        //文件后缀名
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }
}
