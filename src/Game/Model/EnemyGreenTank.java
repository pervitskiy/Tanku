package Game.Model;

import Game.Enemy;
import Level.Level;
import graphics.TextureAtlas;

public class EnemyGreenTank extends Enemy {
    private static final int NORTH_X = 0;
    private static final int NORTH_Y = 11;
    private static final float SPEED = 1.5f;
    private static final int LIVES = 2;
    private static final int BULLET_SPEED = 4;

    public EnemyGreenTank(float x , float y , float scale , TextureAtlas atlas , Level lvl) {
        super(x , y , scale , SPEED , atlas , lvl , NORTH_X , NORTH_Y , LIVES , BULLET_SPEED);

    }
}
