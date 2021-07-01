package chess;

import constant.PlayerType;

/**
 * 兵
 *
 * @author zjx
 * @since 2021/6/8 11:17 下午
 */
public class Soldier extends Chess {

    public Soldier(int i, int x, int y) {
        super(i, x, y);
    }

    @Override
    public boolean isAllowMove(int[][] map, int x, int y) {
        if (Math.abs(x - this.x) > 1 || Math.abs(y - this.y) > 1) return false;

        if (Math.abs(x - this.x) == 1 && Math.abs(y - this.y) == 1) return false;

        if (x > this.x) return false;

        if (x > 4 && y != this.y) return false;
        return true;
    }
}
