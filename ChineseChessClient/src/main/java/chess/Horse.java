package chess;

/**
 * 马
 *
 * @author zjx
 * @since 2021/6/8 11:17 下午
 */
public class Horse extends Chess {

    public Horse(int i, int x, int y) {
        super(i, x, y);
    }

    @Override
    public boolean isAllowMove(int[][] map, int x, int y) {
        if (!((Math.abs(this.x - x) == 2 && Math.abs(this.y - y) == 1)
            || (Math.abs(this.x - x) == 1 && Math.abs(this.y - y) == 2))) {
            return false;
        }

        if (x - this.x == 2 && map[this.x + 1][this.y] != -1) return false;
        if (x - this.x == -2 && map[this.x - 1][this.y] != -1) return false;
        if (y - this.y == 2 && map[this.x][this.y + 1] != -1) return false;
        if (y - this.y == -2 && map[this.x][this.y - 1] != -1) return false;
        return true;
    }
}
