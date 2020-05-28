package dto;

import model.LandEnclosure;

import java.util.List;

/***
 * 宗地附件列表DTO
 */
public class LandEnclosureListDTO {
    private List<LandEnclosure> landEnclosureList;  //宗地附件

    public List<LandEnclosure> getLandEnclosureList() {
        return landEnclosureList;
    }

    public void setLandEnclosureList(List<LandEnclosure> landEnclosureList) {
        this.landEnclosureList = landEnclosureList;
    }
}
