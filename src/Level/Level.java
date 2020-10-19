package Level;

import Game.Game;
import Utils.Utils;
import graphics.TextureAtlas;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {


    public static final int TITLE_SCALE = 8;
    public static final int TITLE_INT_SCALE = 3;
    public static final int SCALED_TILE_SIZE = TITLE_SCALE * TITLE_INT_SCALE;

    private Integer[][] tileMap;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCords;
    private InfoPanel infoPanel;
    private boolean eagleAlive;
    private int count;


    public Level(TextureAtlas atlas , int stage) {
        tiles = new HashMap<TileType, Tile>();
        eagleAlive = true;
        count = 0;
        infoPanel = new InfoPanel(atlas , stage);
        tiles.put(TileType.BRICK , new Tile(atlas.cut(32 * TITLE_SCALE , 0 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.BRICK));
        tiles.put(TileType.METAL , new Tile(atlas.cut(32 * TITLE_SCALE , 2 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.METAL));
        tiles.put(TileType.WATER , new Tile(atlas.cut(32 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.WATER));
        tiles.put(TileType.GRASS , new Tile(atlas.cut(34 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.GRASS));
        tiles.put(TileType.EMRTY , new Tile(atlas.cut(36 * TITLE_SCALE , 6 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.EMRTY));
        tiles.put(TileType.UP_LEFT_EAGLE , new Tile(atlas.cut(38 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.UP_LEFT_EAGLE));
        tiles.put(TileType.UP_RIGHT_EAGLE , new Tile(atlas.cut(39 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.UP_RIGHT_EAGLE));
        tiles.put(TileType.DOWN_LEFT_EAGLE , new Tile(atlas.cut(38 * TITLE_SCALE , 5 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.DOWN_LEFT_EAGLE));
        tiles.put(TileType.DOWN_RIGHT_EAGLE ,
                new Tile(atlas.cut(39 * TITLE_SCALE , 5 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) , TITLE_INT_SCALE ,
                        TileType.DOWN_RIGHT_EAGLE));
        tiles.put(TileType.UP_LEFT_DEAD_EAGLE ,
                new Tile(atlas.cut(40 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) , TITLE_INT_SCALE ,
                        TileType.UP_LEFT_DEAD_EAGLE));
        tiles.put(TileType.UP_RIGHT_DEAD_EAGLE ,
                new Tile(atlas.cut(41 * TITLE_SCALE , 4 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) , TITLE_INT_SCALE ,
                        TileType.UP_RIGHT_DEAD_EAGLE));
        tiles.put(TileType.DOWN_LEFT_DEAD_EAGLE ,
                new Tile(atlas.cut(40 * TITLE_SCALE , 5 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) , TITLE_INT_SCALE ,
                        TileType.DOWN_LEFT_DEAD_EAGLE));
        tiles.put(TileType.DOWN_RIGHT_DEAD_EAGLE ,
                new Tile(atlas.cut(41 * TITLE_SCALE , 5 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) , TITLE_INT_SCALE ,
                        TileType.DOWN_RIGHT_DEAD_EAGLE));
        tiles.put(TileType.OTHER_WATER , new Tile(atlas.cut(33 * TITLE_SCALE , 10 * TITLE_SCALE , TITLE_SCALE , TITLE_SCALE) ,
                TITLE_INT_SCALE , TileType.OTHER_WATER));

        tileMap = Utils.levelParser("res/level" + stage + ".lvl");
        grassCords = new ArrayList<Point>();
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j] == TileType.GRASS.numeric())
;
            }
        }

    }
//блок, который выполняется ещё до создания программы
    //

    public void update(int tileX , int tileY) {
        if (tileMap[tileY][tileX] == TileType.DOWN_LEFT_EAGLE.numeric()
                || tileMap[tileY][tileX] == TileType.DOWN_RIGHT_EAGLE.numeric()
                || tileMap[tileY][tileX] == TileType.UP_LEFT_EAGLE.numeric()
                || tileMap[tileY][tileX] == TileType.UP_RIGHT_EAGLE.numeric())
            destroyEagle();
        else
            tileMap[tileY][tileX] = TileType.EMRTY.numeric();
    }

    public void render(Graphics2D g) {
        count = ++count % 40;

        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if (tile.type() == TileType.WATER && count < 10) {
                    tiles.get(TileType.fromNumeric(5)).render(g , j * SCALED_TILE_SIZE , i * SCALED_TILE_SIZE);
                }
             {
                    if (tile.type() != TileType.GRASS) {

                        tile.render(g , j * SCALED_TILE_SIZE , i * SCALED_TILE_SIZE);

                    }
                }
            }
        }
        infoPanel.renderInfoPanel(g);


    }

    public void renderGrass(Graphics2D g) {
        for (Point p : grassCords) {
            tiles.get(TileType.GRASS).render(g , p.x , p.y);
        }
    }

    private void destroyEagle() {
        for (int i = 0; i < tileMap.length; i++)
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j] == TileType.DOWN_LEFT_EAGLE.numeric())
                    tileMap[i][j] = TileType.DOWN_LEFT_DEAD_EAGLE.numeric();

                else if (tileMap[i][j] == TileType.DOWN_RIGHT_EAGLE.numeric())
                    tileMap[i][j] = TileType.DOWN_RIGHT_DEAD_EAGLE.numeric();

                else if (tileMap[i][j] == TileType.UP_LEFT_EAGLE.numeric())
                    tileMap[i][j] = TileType.UP_LEFT_DEAD_EAGLE.numeric();

                else if (tileMap[i][j] == TileType.UP_RIGHT_EAGLE.numeric())
                    tileMap[i][j] = TileType.UP_RIGHT_DEAD_EAGLE.numeric();
            }
        eagleAlive = false;
        Game.setGameOver();

    }

    public Integer[][] getTileMap() {
        return tileMap;
    }

    public boolean isEagleAlive() {
        return eagleAlive;
    }

}
