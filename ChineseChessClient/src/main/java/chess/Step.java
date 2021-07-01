package chess;

import lombok.Data;

/**
 * 下的每一步
 *
 * @author zjx
 * @since 2021/6/17 下午4:24
 */
@Data
public class Step {

    private int index;

    private int oldX, oldY;

    private int newX, newY;

    private int eatIndex;

    public Step(int index, int oldX, int oldY, int newX, int newY, int eatIndex) {
        this.index = index;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.eatIndex = eatIndex;
    }
}
