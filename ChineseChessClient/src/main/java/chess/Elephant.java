package chess;

import constant.PlayerType;

/**
 * 象
 *
 * @author zjx
 * @since 2021/6/8 11:15 下午
 */
public class Elephant extends Chess {

    public Elephant(int i, int x, int y) {
        super(i, x, y);
    }

    @Override
    public boolean isAllowMove(int[][] map, int x, int y) {
        if (Math.abs(this.x - x) != 2 || Math.abs(this.y - y) != 2) return false;

        if (x < 5) return false;

        if (map[(this.x + x) / 2][(this.y + y) / 2] != -1) return false;
        return true;
    }
}
