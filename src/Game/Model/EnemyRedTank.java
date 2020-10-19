package Game.Model;

import Game.Enemy;
import Level.Level;
import graphics.TextureAtlas;

public class EnemyRedTank extends Enemy {
    private static final int NORTH_X = 8;
    private static final int NORTH_Y = 13;
    private static final float SPEED = 3f;
    private static final int LIVES = 5;
    private static final int BULLET_SPEED = 3;


    public EnemyRedTank(float x , float y , float scale , TextureAtlas atlas , Level lvl) {
        super(x , y , scale , SPEED , atlas , lvl , NORTH_X , NORTH_Y , LIVES , BULLET_SPEED);

    }
}
