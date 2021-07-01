package chess;

/**
 * 炮
 *
 * @author zjx
 * @since 2021/6/8 11:17 下午
 */
public class Cannon extends Chess {
    
    public Cannon(int i, int x, int y) {
        super(i, x, y);
    }
    
    @Override
    public boolean isAllowMove(int[][] map, int x, int y) {
        if (this.x != x && this.y != y) return false;

        int num = 0;
        if (this.x == x) {
            int lowIndex = Math.min(this.y, y);
            int highIndex = Math.max(this.y, y);

            for (int i = lowIndex + 1; i < highIndex; i ++) {
                if (map[x][i] != -1) num ++;
            }
        } else {
            int lowIndex = Math.min(this.x, x);
            int highIndex = Math.max(this.x, x);

            for (int i = lowIndex + 1; i < highIndex; i ++) {
                if (map[i][y] != -1) num ++;
            }
        }

        if (num > 1) return false;
        if (num == 0 && map[x][y] != -1) return false;
        if (num == 1 && map[x][y] == -1) return false;
        return true;
    }
}
