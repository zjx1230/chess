package chess;

import constant.PlayerType;

/**
 * 士
 *
 * @author zjx
 * @since 2021/6/8 11:15 下午
 */
public class Official extends Chess {

    public Official(int i, int x, int y) {
        super(i, x, y);
    }

    @Override
    public boolean isAllowMove(int[][] map, int x, int y) {
        if (y < 3 || y > 5) return false;

        if (x < 7) return false;

        if (Math.abs(x - this.x) == 1 && Math.abs(y - this.y) == 1) return true;
        return false;
    }
}
