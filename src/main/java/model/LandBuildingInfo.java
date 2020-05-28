package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 宗地建筑信息表
 */
@Entity
@Table(name = "land_building_info")
public class LandBuildingInfo implements Serializable {
    private static final long serialVersionUID = 1221974790648200671L;
    @Id
    @Column(length = 36,nullable = false)
    private String id;        //编号,主键
    @Column(length = 50,nullable = false)
    private String landNumber;        //宗地号
    @Column(length = 50)
    private String householdNumber;        //户号
    @Column(length = 4,nullable = false)
    private int floorsNumber;        //建筑层号
    @Column(nullable = false)
    private Double floorsHeight;        //层高，米

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

    public int getFloorsNumber() {
        return floorsNumber;
    }

    public void setFloorsNumber(int floorsNumber) {
        this.floorsNumber = floorsNumber;
    }

    public Double getFloorsHeight() {
        return floorsHeight;
    }

    public void setFloorsHeight(Double floorsHeight) {
        this.floorsHeight = floorsHeight;
    }

    public String getHouseholdNumber() {
        return householdNumber;
    }

    public void setHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
    }
}
