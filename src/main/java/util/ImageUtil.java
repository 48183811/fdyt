package util;
import dto.AuthCodeDTO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.Random;

public class ImageUtil {
    public static BufferedImage change2jpg(File f) {
        try {
            Image i = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            pg.grabPixels();
            int width = pg.getWidth(), height = pg.getHeight();
            final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            BufferedImage img = new BufferedImage(RGB_OPAQUE, raster, false, null);
            return img;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****
     * 生成图片验证码
     * @return
     */
    public static AuthCodeDTO authCode(){
        Random random=new Random();

        //内存图片对象(TYPE_INT_BGR 选择图片模式RGB模式)
        BufferedImage image = new BufferedImage(63,37,BufferedImage.TYPE_INT_BGR);
        //得到画笔
        Graphics graphics = image.getGraphics();
        //画之前要设置颜色，设置画笔颜色
        graphics.setColor(Color.PINK);
        //填充矩形区域（指定要画的区域设置区）
        graphics.fillRect(0,0,63,37);
        //为了防止黑客软件通过扫描软件识别验证码。要在验证码图片上加干扰线
        //给两个点连一条线graphics.drawLine();
        for (int i=0;i<10;i++){
            //颜色也要随机（设置每条线随机颜色）
            graphics.setColor(getRandomColor());
            int x1=random.nextInt(63);
            int y1=random.nextInt(37);
            int x2=random.nextInt(63);
            int y2=random.nextInt(37);
            graphics.drawLine(x1,y1,x2,y2);
        }

        //拼接4个验证码，画到图片上
        char [] arrays={'0','1','2','3','4','5','6','7','8','9'};
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<4;i++){
            //设置字符的颜色
            int index=random.nextInt(arrays.length);
            builder.append(arrays[index]);
        }

        //将4个字符画到图片上graphics.drawString(str ,x,y);一个字符一个字符画
        for (int i=0;i<builder.toString().length();i++)
        {
            graphics.setFont(new Font("宋体",Font.BOLD,30));
            graphics.setColor(getRandomColor());
            char item=builder.toString().charAt(i);
            graphics.drawString(item+"",i*15,30);
        }

        AuthCodeDTO authCodeDTO = new AuthCodeDTO();
        authCodeDTO.setImage(image);
        authCodeDTO.setAuthCode(builder.toString());
        return authCodeDTO;
    }

    /***
     * 生成随机色
     * @return
     */
    private static Color getRandomColor(){
        Random random=new Random();
        int r=random.nextInt(256);
        int g=random.nextInt(256);
        int b=random.nextInt(256);
        return new Color(r,g,b);
    }
}