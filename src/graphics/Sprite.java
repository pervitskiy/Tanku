package graphics;

import Utils.Utils;

import javax.rmi.CORBA.Util;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {
    private SpriteSheet sheet;
    private float scale;
    private BufferedImage image;

    public Sprite(SpriteSheet sheet, float scale) {
        this(sheet, scale, 0, true);
    }



    public Sprite(SpriteSheet sheet,float scale,  int spriteNumber, boolean alpha){
        this.sheet=sheet;
        this.scale=scale;
        image = sheet.getSprite(spriteNumber);
        image= Utils.resize(image, (int )(image.getWidth()*scale),(int)(image.getHeight() * scale));

        if(alpha)
            for (int i = 0; i < image.getHeight(); i++)
                for (int j = 0; j < image.getWidth(); j++) {
                    int pixel = image.getRGB(j, i);
                    if ((pixel & 0x00FFFFFF) < 10)
                        image.setRGB(j, i, (pixel & 0x00FFFFFF));
                }

    }

    public void render(Graphics2D g, float x, float y){
        //увеличиваем картинку
        g.drawImage(image, (int)(x), (int)(y), null);
    }
    public void getSprite(int i){
          this.image=sheet.getSprite(i);
        image= Utils.resize(image, (int )(image.getWidth()*scale),(int)(image.getHeight() * scale));

    }
}
