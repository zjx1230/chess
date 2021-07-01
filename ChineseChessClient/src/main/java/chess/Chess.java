package chess;

import constant.ChessType;
import constant.PlayerType;
import constant.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/8 11:06 下午
 */
public abstract class Chess {

    protected short player; // 所属方：红或黑

    protected int x, y;   // 坐标

    protected Image chessImage;

    public Chess(int x, int y) {
        this.x = x; this.y = y;
        player = -1;
    }

    public Chess(int i, int x, int y) {
        this.x = x; this.y = y;
        this.player = i > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER;
        this.chessImage = Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource(ChessType.chessMap.get(ChessType.chessType[i] + this.player)));
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public short getPlayer() {
        return this.player;
    }

    public void setPlayer(short player) {
        this.player = player;
    }

    public void reversePos() {
        this.x = 9 - x;
        this.y = 8 - y;
    }

    public void paint(Graphics g, JPanel observer) {
        g.drawImage(chessImage, Position.LEFT_X + y * Position.Y_WIDTH, Position.LEFT_Y + x * Position.X_WIDTH, Position.X_WIDTH, Position.Y_WIDTH, (ImageObserver) observer);
    }

    public void drawSelectedChess(Graphics g) {
        g.drawRect(Position.LEFT_X + y * Position.Y_WIDTH, Position.LEFT_Y + x * Position.X_WIDTH, Position.X_WIDTH, Position.Y_WIDTH);
    }

    public abstract boolean isAllowMove(int[][] map, int x, int y);
}
