package Game;

import Game.Model.EnemyGreenTank;
import Game.Model.EnemyInfantryVehicle;
import Game.Model.EnemyRedTank;
import Game.Model.EnemyTank;
import IO.Input;
import Level.Level;
import Utils.Time;
import Utils.Utils;
import com.display.Display;
import graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

public class Game implements Runnable {
    public static final int WIDTH = 624;
    public static final int HEIGHT = 624;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 5;
    public static final float SCALE = 3f;
    //сколько раз в секунду мы хотим считать нашу физику игры
    public static final float UPDATE_RATE = 60.0f;
    //какой интервал должен быть между перерасчетом физики
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;
    public static final String ATLAS_FILE_NAME = "texture_atlas.png";
    private static final float PLAYER_SPEED = 3f;
    private static final float PLAYER_SPEED_2 = 2f;

    private static int stage = 1;

    private static boolean gameOver;
    private static List<Enemy> enemyList = new LinkedList<>();
    private static int enemyCount;
    private static Map<EntityType, List<Bullet>> bullets;
    private static boolean pause;
    private BufferedImage gameOverImage;
    private BufferedImage pauseImage;
    private long timeWin;
    private boolean canCreateEnemy;
    private boolean running;
    private Thread gameThread;
    private Input input;
    private Graphics2D graphics;
    private TextureAtlas atlas;
    private Player player;
    private Player2 player2;
    private Level level;


    public Game() {
        //игра ещё не бежит
        running = false;
        Display.create(WIDTH + 8 * Level.SCALED_TILE_SIZE , HEIGHT , TITLE , CLEAR_COLOR , NUM_BUFFERS);
        input = new Input();
        Display.addInputListener(input);
        graphics = Display.getGraphics();
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        level = new Level(atlas , stage);
        player = new Player(SCALE , PLAYER_SPEED , atlas , level);
        player2 = new Player2(SCALE , PLAYER_SPEED_2 , atlas , level);
        enemyCount = 20;
        pause = false;
        timeWin = 0;
        bullets = new HashMap<>();
        bullets.put(EntityType.Player , new LinkedList<Bullet>());
        bullets.put(EntityType.Player2 , new LinkedList<Bullet>());

        bullets.put(EntityType.Enemy , new LinkedList<Bullet>());

        gameOver = false;
        gameOverImage = Utils.resize(
                atlas.cut(36 * Level.TITLE_SCALE , 23 * Level.TITLE_SCALE , 4 * Level.TITLE_SCALE , 2 * Level.TITLE_SCALE) ,
                4 * Level.SCALED_TILE_SIZE , 2 * Level.SCALED_TILE_SIZE);
        for (int i = 0; i < gameOverImage.getHeight(); i++)
            for (int j = 0; j < gameOverImage.getWidth(); j++) {
                int pixel = gameOverImage.getRGB(j , i);
                if ((pixel & 0x00FFFFFF) < 10)
                    gameOverImage.setRGB(j , i , (pixel & 0x00FFFFFF));
            }
        pauseImage = Utils.resize(
                atlas.cut(36 * Level.TITLE_SCALE , 22 * Level.TITLE_SCALE , 5 * Level.TITLE_SCALE , 1 * Level.TITLE_SCALE) ,
                5 * Level.SCALED_TILE_SIZE , 1 * Level.SCALED_TILE_SIZE);
        for (int i = 0; i < pauseImage.getHeight(); i++)
            for (int j = 0; j < pauseImage.getWidth(); j++) {
                int pixel = pauseImage.getRGB(j , i);
                if ((pixel & 0x00FFFFFF) < 10)
                    pauseImage.setRGB(j , i , (pixel & 0x00FFFFFF));
            }

    }

    public static void setGameOver() {
        gameOver = true;

    }

    public static List<Enemy> getEnemies() {
        return enemyList;
    }

    public static void registerBullet(EntityType type , Bullet bullet) {
        bullets.get(type).add(bullet);
    }

    public static List<Bullet> getBullets(EntityType type) {
        return bullets.get(type);
    }

    public static void unregisterBullet(EntityType type , Bullet bullet) {
        if (bullets.get(type).size() > 0) {
            bullets.get(type).remove(bullet);
        }
    }

    public static int getEnemyCount() {
        return enemyCount;
    }

    public static void setStage() {
        //    String jsonStr = new Gson().toJson(bullets.get(EntityType.Player).get());
        //  System.out.print(jsonStr);
    }

    public static void getPause() {
        pause = true;
    }

    public static void setPause() {
        pause = true;
    }

    // не может выволняться в двух процессах
    public synchronized void start() {
        if (running)
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();//запустили метод run
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cleanUp();

    }

    //будет считать всю физику
    private void update() {

        if (!input.getKey(KeyEvent.VK_ESCAPE)) {
            pause = true;
            if (enemyList.size() == 0 && enemyCount == 0 && timeWin == 0)
                timeWin = System.currentTimeMillis();
            if (enemyList.size() == 0 && enemyCount == 0 && (player.hasMoreLives() || player2.hasMoreLives()) && !gameOver)
                nextLevel();

            canCreateEnemy = true;

            if (enemyList.size() < 4 && enemyCount > 0) {
                Random rand = new Random();
                float possibleX = rand.nextInt(3) * ((Game.WIDTH - Enemy.SPRITE_SCALE * Game.SCALE) / 2);
                Rectangle2D.Float recForX = new Rectangle2D.Float(possibleX , 0 , Player.SPRITE_SCALE * Game.SCALE ,
                        Player.SPRITE_SCALE * Game.SCALE);
                for (Enemy enemy : enemyList) {
                    if (enemy.isEvolving()) {
                        canCreateEnemy = false;
                        break;
                    }

                    if (canCreateEnemy)
                        if (recForX.intersects(enemy.getRectangle())) {
                            canCreateEnemy = false;
                        }

                }
                if (canCreateEnemy) {
                    if (player != null)
                        if (recForX.intersects(player.getRectangle())) {
                            canCreateEnemy = false;
                        }
                    if (player2 != null)
                        if (recForX.intersects(player2.getRectangle())) {
                            canCreateEnemy = false;
                        }
                    if (canCreateEnemy) {
                        Enemy enemy;
                        enemyCount--;
                        if (stage == 3) {
                            if (enemyCount < 3)
                                enemy = new EnemyInfantryVehicle(possibleX , 0 , SCALE , atlas , level);
                            else
                                enemy = new EnemyTank(possibleX , 0 , SCALE , atlas , level);
                        } else if (stage == 2) {
                            Random random = new Random();
                            switch (random.nextInt(5)) {
                                case 0:
                                    enemy = new EnemyInfantryVehicle(possibleX , 0 , SCALE , atlas , level);
                                    break;
                                case 1:
                                    enemy = new EnemyGreenTank(possibleX , 0 , SCALE , atlas , level);
                                    break;
                                default:
                                    enemy = new EnemyTank(possibleX , 0 , SCALE , atlas , level);
                            }
                        } else {
                            Random rd = new Random();
                            switch (rd.nextInt(3)) {
                                case 1:
                                    enemy = new EnemyGreenTank(possibleX , 0 , SCALE , atlas , level);
                                    break;
                                case 0:
                                    enemy = new EnemyRedTank(possibleX , 0 , SCALE , atlas , level);
                                    break;
                                default:
                                    enemy = new EnemyTank(possibleX , 0 , SCALE , atlas , level);
                            }
                        }
                        enemy.setPlayer(player);
                        enemy.setPlayer2(player2);
                        enemyList.add(enemy);

                    }
                }
            }

            player2.setPlayer(player);
            if (player != null)
                player.setPlayer(player2);
            List<Bullet> playerBulletList = getBullets(EntityType.Player);
            Stream stream = playerBulletList.stream();
            if (playerBulletList.size() > 0) {
                for (Enemy enemy : enemyList) {
                    if (enemy.isEvolving())
                        continue;
                    if (enemy.getRectangle().intersects(playerBulletList.get(0).getRectangle()) && playerBulletList.get(0).isActive()) {
                        enemy.fixHitting(Player.getPlayerStrength());
                        playerBulletList.get(0).setInactive();
                        if (!enemy.hasMoreLives())
                            enemy.setDead();
                    }
                }
            }

            List<Bullet> player2BulletList = getBullets(EntityType.Player2);
            if (player2BulletList.size() > 0) {
                for (Enemy enemy : enemyList) {
                    if (enemy.isEvolving())
                        continue;
                    if (enemy.getRectangle().intersects(player2BulletList.get(0).getRectangle()) && player2BulletList.get(0).isActive()) {
                        enemy.fixHitting(Player2.getPlayerStrength());
                        player2BulletList.get(0).setInactive();
                        if (!enemy.hasMoreLives())
                            enemy.setDead();
                    }
                }
            }
            for (Enemy enemy : enemyList)
                enemy.update(input);

            for (int i = 0; i < bullets.get(EntityType.Enemy).size(); i++)
                bullets.get(EntityType.Enemy).get(i).update();

            for (int i = 0; i < bullets.get(EntityType.Player).size(); i++)
                bullets.get(EntityType.Player).get(i).update();

            for (int i = 0; i < bullets.get(EntityType.Player2).size(); i++)
                bullets.get(EntityType.Player2).get(i).update();

            if (player != null && !player.hasMoreLives())
                player = null;

            if (player2 != null && !player2.hasMoreLives())
                player2 = null;
            if (!gameOver) {
                if (player != null)
                    player.update(input);

                if (player2 != null && !player2.hasMoreLives())
                    player2 = null;


                if (player2 != null)
                    player2.update(input);
            }
        } else pause = false;
    }

    //отрисовка нужной нам сцены
    private void render() {
        Display.clear();


        level.render(graphics);


        if (player != null) {
            if (!player.isAlive()) {
                player.drawExplosion(graphics);
            } else
                player.render(graphics);
        }
        if (player2 != null) {
            if (!player2.isAlive()) {
                player2.drawExplosion(graphics);
            } else
                player2.render(graphics);
        }


        for (int i = 0; i < enemyList.size(); i++) {
            if (!enemyList.get(i).isAlive()) {
                enemyList.get(i).drawExplosion(graphics);
                enemyList.remove(i);
            }
        }


        for (Enemy enemy : enemyList)
            enemy.render(graphics);


        for (int i = 0; i < bullets.get(EntityType.Enemy).size(); i++)
            bullets.get(EntityType.Enemy).get(i).render(graphics);

        for (int i = 0; i < bullets.get(EntityType.Player).size(); i++)
            bullets.get(EntityType.Player).get(i).render(graphics);

        for (int i = 0; i < bullets.get(EntityType.Player2).size(); i++)
            bullets.get(EntityType.Player2).get(i).render(graphics);


        level.renderGrass(graphics);


        if (gameOver) {
            graphics.drawImage(gameOverImage , Game.WIDTH / 2 - 2 * Level.SCALED_TILE_SIZE , Game.HEIGHT / 2 , null);
        }
        if (!pause) {
            graphics.drawImage(pauseImage , Game.WIDTH / 2 - 2 * Level.SCALED_TILE_SIZE , Game.HEIGHT / 2 , null);
        }


        //закончили рисовать нашу сцену и ее необходимо показать
        Display.swapBuffers();



    }

    private void cleanUp() {
        Display.destroy();
    }

    @Override
    public void run() {
        int fps = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        float delta = 0;
        long lastTime = Time.get();
        //делаем бесконечный цикл
        while (running) {
            long now = Time.get();
            long intervalTime = now - lastTime;
            lastTime = now;
            count += intervalTime;
            boolean render = false;
            //каждая ровная 1 в дельта обозначает что нужно сделать update
            delta += (intervalTime / UPDATE_INTERVAL);
            //на тот случай, если уже надо сделать несколько обновланией
            while (delta > 1) {
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }
            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || FPS: " + fps + "| Upd: " + upd + "Updl: " + updl);
                fps = 0;
                upd = 0;
                updl = 0;
                count = 0;
            }

        }
    }

    private void nextLevel() {

        if (timeWin == 0 || System.currentTimeMillis() < timeWin + 5000)
            return;

        bullets = new HashMap<>();
        bullets.put(EntityType.Player , new LinkedList<Bullet>());
        bullets.put(EntityType.Player2 , new LinkedList<Bullet>());
        bullets.put(EntityType.Enemy , new LinkedList<Bullet>());
        if (++stage > 3)
            stage = 1;
        level = new Level(atlas , stage);
        enemyCount = 20;
        enemyList = new LinkedList<>();
        player.moveOnNextLevel();
        player2.moveOnNextLevel();

        timeWin = 0;

    }


}
