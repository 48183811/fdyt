package dto;

/***
 * 房地一体shp数据信息DTO
 */
public class FDYT_DTO {
    private Integer gid;        //主键
    private String name;        //权利人姓名
    private String qsbh;        //宗地号
    private String idCard;        //权利人身份证号
    private String dlbm;        //地类编码
    private String coordinates;   //范围坐标串
    private Double area;       //面积，平方米

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQsbh() {
        return qsbh;
    }

    public void setQsbh(String qsbh) {
        this.qsbh = qsbh;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDlbm() {
        return dlbm;
    }

    public void setDlbm(String dlbm) {
        this.dlbm = dlbm;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
