package dto;

import java.awt.image.BufferedImage;

public class AuthCodeDTO {
    private BufferedImage image;    //图片验证码
    private String authCode;        //验证码

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
