package graphics;

import Utils.ResourceLoader;

import java.awt.image.BufferedImage;

public class TextureAtlas {
    BufferedImage image;
    public TextureAtlas(String imageName){
        image = ResourceLoader.loadImage(imageName);
    }
    //вырезаем куски из картинки
    public BufferedImage cut( int x, int y, int w, int h){
      return  image.getSubimage(x,y,w,h);
    }
}
