package Level;

import Utils.Utils;
import graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private TileType type;
//используем спрайтШит когда будет готова анимация
    protected Tile(BufferedImage image, int scale,  TileType type ){
        this.type=type;
        this.image= Utils.resize(image,image.getWidth()* scale, image.getHeight()*scale);
        //меняем прозрачность картиники если это трава
        if (type == TileType.GRASS)
            for (int i = 0; i < this.image.getHeight(); i++)
                for (int j = 0; j < this.image.getWidth(); j++) {
                    int pixel = this.image.getRGB(j, i);
                    if ((pixel & 0x00FFFFFF) < 1)
                        this.image.setRGB(j, i, (pixel & 0x00FFFFFF));
                }

    }
    protected  TileType type(){
        return type;
    }
    protected void render(Graphics2D g, int x, int y){
        g.drawImage(image, x,y, null);
    }





}
