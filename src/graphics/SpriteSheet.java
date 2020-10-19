package graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sheet;
    private int spriteCount;
    //размер одного спрайта
    private int scale;
    private int spritesInWidth;

    public SpriteSheet(BufferedImage sheet, int spriteCount, int scale){
        this.sheet=sheet;
        this.spriteCount = spriteCount;
        this.scale=scale;

        this.spritesInWidth = sheet.getWidth()/scale;
    }

    public BufferedImage getSprite(int index){
        index = index % spriteCount;

        //получение номера картинки, необходимую вырезать

        int x=index %spritesInWidth * scale ;
        int y=index / spritesInWidth * scale;

        return sheet.getSubimage(x,y,scale,scale);
    }


}
