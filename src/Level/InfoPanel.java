package Level;

import Game.*;
import graphics.Sprite;
import graphics.SpriteSheet;
import graphics.TextureAtlas;
import Utils.*;


import java.awt.*;

public class InfoPanel {

    private TextureAtlas atlas;
    private Sprite enemySprite;
    private SpriteSheet numbersToFour;
    private SpriteSheet		numbersToNine;
    private int				stage;

    InfoPanel(TextureAtlas atlas, int stage) {
        this.atlas = atlas;
        this.stage = stage%10;

        numbersToFour = new SpriteSheet(
                atlas.cut(41 * Level.TITLE_SCALE, 23 * Level.TITLE_SCALE, 5 * Level.TITLE_SCALE, Level.TITLE_SCALE), 5,
                Level.TITLE_SCALE);
        numbersToNine = new SpriteSheet(
                atlas.cut(41 * Level.TITLE_SCALE, 24 * Level.TITLE_SCALE, 5 * Level.TITLE_SCALE, Level.TITLE_SCALE), 5,
                Level.TITLE_SCALE);
        enemySprite = new Sprite(new SpriteSheet(
                atlas.cut(40 * Level.TITLE_SCALE, 24 * Level.TITLE_SCALE, Level.TITLE_SCALE, Level.TITLE_SCALE), 1,
                Level.TITLE_SCALE), Level.TITLE_INT_SCALE, 0, false);
    }

    public void renderInfoPanel(Graphics2D g) {
        g.drawImage(Utils.resize(atlas.cut(46 * Level.TITLE_SCALE, 0, Level.TITLE_SCALE, Level.TITLE_SCALE),
                8 * Level.SCALED_TILE_SIZE, Game.HEIGHT), Game.WIDTH, 0, null);

        g.drawImage(
                Utils.resize(
                        atlas.cut(41 * Level.TITLE_SCALE, 22 * Level.TITLE_SCALE, 5 * Level.TITLE_SCALE, Level.TITLE_SCALE),
                        5 * Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE),
                Game.WIDTH + Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE, null);

        new Sprite(stage< 5 ? numbersToFour : numbersToNine, Level.TITLE_INT_SCALE, stage%5, false).
                render(g, Game.WIDTH + 6 * Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE);

        g.drawImage(
                Utils.resize(
                        atlas.cut(47 * Level.TITLE_SCALE, 17 * Level.TITLE_SCALE, 2 * Level.TITLE_SCALE, Level.TITLE_SCALE),
                        2 * Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE),
                Game.WIDTH + 3 * Level.SCALED_TILE_SIZE, 15 * Level.SCALED_TILE_SIZE, null);

        g.drawImage(
                Utils.resize(
                        atlas.cut(47 * Level.TITLE_SCALE, 18 * Level.TITLE_SCALE, Level.TITLE_SCALE, Level.TITLE_SCALE),
                        Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE),
                Game.WIDTH + 3 * Level.SCALED_TILE_SIZE, 16 * Level.SCALED_TILE_SIZE, null);

        g.drawImage(
                Utils.resize(
                        atlas.cut(47 * Level.TITLE_SCALE, 17 * Level.TITLE_SCALE, 2 * Level.TITLE_SCALE, Level.TITLE_SCALE),
                        2 * Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE),
                Game.WIDTH + 3 * Level.SCALED_TILE_SIZE, 18 * Level.SCALED_TILE_SIZE, null);

        g.drawImage(
                Utils.resize(
                        atlas.cut(47 * Level.TITLE_SCALE, 18 * Level.TITLE_SCALE, Level.TITLE_SCALE, Level.TITLE_SCALE),
                        Level.SCALED_TILE_SIZE, Level.SCALED_TILE_SIZE),
                Game.WIDTH + 3 * Level.SCALED_TILE_SIZE, 19 * Level.SCALED_TILE_SIZE, null);

        int playerLives = Player.getPlayerLives()<0?0: Player.getPlayerLives();
        int player2Lives = Player2.getPlayerLives()<0?0: Player2.getPlayerLives();


        new Sprite( playerLives< 5 ? numbersToFour : numbersToNine, Level.TITLE_INT_SCALE,
                playerLives % 5, false).render(g, Game.WIDTH + 4 * Level.SCALED_TILE_SIZE,
                16 * Level.SCALED_TILE_SIZE);

        new Sprite( player2Lives< 5 ? numbersToFour : numbersToNine, Level.TITLE_INT_SCALE,
                player2Lives % 5, false).render(g, Game.WIDTH + 4 * Level.SCALED_TILE_SIZE,
                19 * Level.SCALED_TILE_SIZE);


        for (int i = 0; i < Game.getEnemyCount(); i++) {
            enemySprite.render(g, Game.WIDTH + 3 * Level.SCALED_TILE_SIZE + i % 2 * Level.SCALED_TILE_SIZE,
                    3 * Level.SCALED_TILE_SIZE + i / 2 * Level.SCALED_TILE_SIZE);
        }

    }

}
