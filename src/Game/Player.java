package Game;

import IO.Input;
import Level.Level;
import graphics.Sprite;
import graphics.SpriteSheet;
import graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Entity {
    private static final float APPEARANCE_X = Entity.SPRITE_SCALE * Game.SCALE * 4;
    private static final float APPEARANCE_Y = Entity.SPRITE_SCALE * Game.SCALE * 12;
    private static final int PROTECTION_TIME = 4000;

    private static int lives;
    private static int strength;
    private Heading heading;
    private Map<Heading, Sprite> spriteMap;
    private float scale;
    private float speed;
    private boolean isProtected;
    private List<Sprite> protectionList;
    private Bullet bullet;
    private float bulletSpeed;
    private int count;
    private static Player2 player2;




    public Player(float scale , float speed , TextureAtlas atlas , Level lvl) {
        super(EntityType.Player , APPEARANCE_X , APPEARANCE_Y , scale , atlas , lvl);
        this.scale = scale;
        this.speed = speed;
        lives = 2;
        strength = 1;
        heading = Heading.NORTH;
        bulletSpeed = 6;
        count=0;

        spriteMap = new HashMap<Heading, Sprite>();
        isProtected = true;
        protectionList = new ArrayList<>();
        protectionList.add(
                new Sprite(new SpriteSheet(atlas.cut(16 * SPRITE_SCALE , 9 * SPRITE_SCALE , SPRITE_SCALE , SPRITE_SCALE) ,
                        SPRITES_PER_HEADING , SPRITE_SCALE) , scale));
        protectionList.add(
                new Sprite(new SpriteSheet(atlas.cut(17 * SPRITE_SCALE , 9 * SPRITE_SCALE , SPRITE_SCALE , SPRITE_SCALE) ,
                        SPRITES_PER_HEADING , SPRITE_SCALE) , scale));

        for (Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas) , SPRITES_PER_HEADING_FOR_ENTITY , SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet , scale);
            spriteMap.put(h , sprite);
        }
    }

    public static int getPlayerLives() {
        return lives;
    }

    public static int getPlayerStrength() {
        return strength;
    }

    @Override
    public synchronized void  update(Input input) {

        if (!lvl.isEagleAlive())
            return;

         if (evolving)
            return;

        if (System.currentTimeMillis() > createdTime + EVOLVING_TIME + PROTECTION_TIME)
            isProtected = false;

        float newX = x;
        float newY = y;

        if (input.getKey(KeyEvent.VK_UP)) {
            newY -= speed;
            newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;

            heading = Heading.NORTH;
            spriteMap.get(Heading.NORTH).getSprite(++count);
        } else if (input.getKey(KeyEvent.VK_RIGHT)) {
            newX += speed;
            newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.EAST;
            spriteMap.get(Heading.EAST).getSprite(++count);
        } else if (input.getKey(KeyEvent.VK_LEFT)) {
            newX -= speed;
            newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.WEST;
            spriteMap.get(Heading.WEST).getSprite(++count);
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            newY += speed;
            newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
            heading = Heading.SOUTH;
            spriteMap.get(Heading.SOUTH).getSprite(++count);
        }


        if (newX < 0) {
            newX = 0;
        } else if (newX >= Game.WIDTH - SPRITE_SCALE * scale) {
            newX = Game.WIDTH - SPRITE_SCALE * scale;
        }
        if (newY < 0) {
            newY = 0;
        } else if (newY >= Game.HEIGHT - SPRITE_SCALE * scale) {
            newY = Game.HEIGHT - SPRITE_SCALE * scale;
        }

        switch (heading) {
            case NORTH:
                if (canMove(newX , newY , newX + (SPRITE_SCALE * scale / 2) , newY , newX + (SPRITE_SCALE * scale) , newY) && !intersectsEnemy(newX , newY)
                        && (player2 == null || !getRectangle(newX , newY).intersects(player2.getRectangle()))) {
                    x = newX;
                    y = newY;
                }
                break;
            case EAST:
                if (canMove(newX + (SPRITE_SCALE * scale) , newY , newX + (SPRITE_SCALE * scale) ,
                        newY + (SPRITE_SCALE * scale / 2) , newX + (SPRITE_SCALE * scale) , newY + (SPRITE_SCALE * scale))&& !intersectsEnemy(newX , newY)
                        && (player2 == null || !getRectangle(newX , newY).intersects(player2.getRectangle()))) {
                    x = newX;
                    y = newY;
                }
                break;
            case WEST:
                if (canMove(newX , newY , newX , newY + (SPRITE_SCALE * scale / 2) , newX , newY + (SPRITE_SCALE * scale))&& !intersectsEnemy(newX , newY)
                        && (player2 == null || !getRectangle(newX , newY).intersects(player2.getRectangle()))) {
                    x = newX;
                    y = newY;
                }
                break;
            case SOUTH:
                if (canMove(newX , newY + (SPRITE_SCALE * scale) , newX + (SPRITE_SCALE * scale / 2) ,
                        newY + (SPRITE_SCALE * scale) , newX + (SPRITE_SCALE * scale) , newY + (SPRITE_SCALE * scale))&& !intersectsEnemy(newX , newY)
                        && (player2 == null || !getRectangle(newX , newY).intersects(player2.getRectangle()))) {
                    x = newX;
                    y = newY;
                }
                break;
        }
        List<Bullet> bullets = Game.getBullets(EntityType.Enemy);
        if (bullets != null) {
            for (Bullet enemyBullet : bullets) {
                if (getRectangle().intersects(enemyBullet.getRectangle()) && enemyBullet.isActive()) {
                    if (!isProtected)
                        isAlive = false;
                    enemyBullet.setInactive();
                }

            }

        }

        if (input.getKey(KeyEvent.VK_ENTER)) {
            if (bullet == null || !bullet.isActive()) {
                if (Game.getBullets(EntityType.Player).size() == 0) {
                    bullet = new Bullet(x , y , scale , bulletSpeed , heading.toString().substring(0 , 4) , atlas , lvl ,
                            EntityType.Player);
                }
            }
        }


    }


    @Override
    public void render(Graphics2D g) {
        if (evolving) {
            drawEvolving(g);
            return;
        }
        spriteMap.get(heading).render(g , x , y);

         if (isProtected)
              drawProtection(g);
    }

    private void drawProtection(Graphics2D g) {
        if (animationCount % 16 < 8)
            protectionList.get(0).render(g , x , y);
        else
            protectionList.get(1).render(g , x , y);
        animationCount++;

    }

    @Override
    public void drawExplosion(Graphics2D g) {
        super.drawExplosion(g);
        if (--lives >= 0)
            reset();
        else
            Game.setGameOver();
    }
    public void reset() {
        this.x = APPEARANCE_X;
        this.y = APPEARANCE_Y;
        isAlive = true;
        evolving = true;
        isProtected = true;
        createdTime = System.currentTimeMillis();
        strength = 1;
        heading = Heading.NORTH;

    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }
    public boolean hasMoreLives() {
        return lives >= 0;
    }
    private boolean intersectsEnemy(float newX , float newY) {
        List<Enemy> enemyList = Game.getEnemies();
        Rectangle2D.Float rect = getRectangle(newX , newY);
        for (Enemy enemy : enemyList) {
            if (rect.intersects(enemy.getRectangle()))
                return true;
        }
        return false;
    }

    public void moveOnNextLevel() {
        this.x = APPEARANCE_X;
        this.y = APPEARANCE_Y;
        evolving = true;
        isProtected = true;
        bullet = null;
        createdTime = System.currentTimeMillis();

        heading = Heading.NORTH;



        }

    public void setPlayer(Player2 player) {
        Player.player2 = player;
    }




    private enum Heading {
        NORTH(0 * SPRITE_SCALE , 0 * SPRITE_SCALE , 1 * SPRITE_SCALE , 2 * SPRITE_SCALE),
        EAST(6 * SPRITE_SCALE , 0 * SPRITE_SCALE , 1 * SPRITE_SCALE , 2 * SPRITE_SCALE),
        SOUTH(4 * SPRITE_SCALE , 0 * SPRITE_SCALE , 1 * SPRITE_SCALE , 2 * SPRITE_SCALE),
        WEST(2 * SPRITE_SCALE , 0 * SPRITE_SCALE , 1 * SPRITE_SCALE , 2 * SPRITE_SCALE);

        private int x, y, h, w;

        Heading(int x , int y , int h , int w) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
        }

        //для кажжого направления будут вырещаться свои спрайты

        protected BufferedImage texture(TextureAtlas atlas) {
            return atlas.cut(x , y , w , h);
        }

    }
}
