package view;

import chess.Chess;
import chess.ChessFactory;
import audio.AudioUtil;
import chess.Step;
import constant.PlayerType;
import constant.Position;
import constant.ResultType;
import net.NetClient;
import util.TimerUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 棋盘
 *
 * @author zjx
 * @since 2021/6/8 11:05 下午
 */
public class ChessBoard extends JPanel {

    private int[][] map = new int[10][9];   // 用于存储当前的盘面

    private Chess[] chess = new Chess[32];  // 棋子

    private short ownerPlayer;              // 当前所属方

    private String message;                 // 存储要显示的提示信息

    private AtomicBoolean isMyTurn;               // 是否轮到自己
    
    private boolean isFirstClick = true;    // 是否属于第一次选择棋子，下一步需要有选棋和动作两部分

    private Chess firstChess, secondChess;  // 用于下一步棋使用

    private NetClient netClient = NetClient.getNetClient();

    private WinLostDialog winLostDialog;

    private ArrayList<Step> steps;

    public ChessBoard() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isMyTurn != null && isMyTurn.get()) {
                    runChess(e);
                    repaint();
                }
            }

            private void runChess(MouseEvent e) {
                if (isFirstClick) {
                    firstChess = getChess(e.getX(), e.getY());
                    if (firstChess == null || firstChess.getPlayer() == -1 || firstChess.getPlayer() != ownerPlayer) {
                        firstChess = null;
                        return;
                    }

                    isFirstClick = false;
                } else {
                    secondChess = getChess(e.getX(), e.getY());
                    if (secondChess == null || firstChess == secondChess) {
                        return;
                    }

                    if (secondChess.getPlayer() == ownerPlayer) {    // 重新选了新的棋子
                        firstChess = secondChess;
                        secondChess = null;
                        return;
                    }

                    if (firstChess.isAllowMove(map, secondChess.getX(), secondChess.getY())) {
                        int firstX = firstChess.getX(), firstY = firstChess.getY();
                        int secondX = secondChess.getX(), secondY = secondChess.getY();
                        boolean isWin = false;

                        if (ownerPlayer == PlayerType.RED_PLAYER && map[secondX][secondY] == 0) {
                            // 赢了
                            isWin = true;
                            winLostDialog = new WinLostDialog(GameClient.getInstance(), "恭喜你，获胜！");
                        }

                        if (ownerPlayer == PlayerType.BLACK_PLAYER && map[secondX][secondY] == 16) {
                            // 赢了
                            isWin = true;
                            winLostDialog = new WinLostDialog(GameClient.getInstance(), "恭喜你，获胜！");
                        }

                        if (secondChess.getPlayer() == -1) {    // 移动棋子
                            int firstIndex = map[firstX][firstY];
                            swap(firstX, firstY, secondX, secondY);
                            secondChess = chess[map[secondX][secondY]];
                            secondChess.setPos(secondX, secondY);
                            // 看一下这一步走后会不会造成将与被将的情况
                            int res = analyse();
                            if (res >= 2) {
                                swap(secondX, secondY, firstX, firstY);
                                secondChess.setPos(firstX, firstY);
                                GameClient gameClient = GameClient.getInstance();
                                Opane.notice(gameClient, "将会被将，不允许");
                                return;
                            }

                            steps.add(new Step(firstIndex, firstX, firstY, secondX, secondY, -1));
                            firstChess = null;
                            // 播放落子声音
                            AudioUtil.playLuoZi();
                            if (isWin) {
                                repaint();
                                GameClient gameClient = GameClient.getInstance();
                                //netClient.sendRunCommand(gameClient.getEnemyId(), 9 - firstX, 8 - firstY, 9 - secondX, 8 - secondY);
                                netClient.sendWinCommand(gameClient.getUserId(), gameClient.getEnemyId(), 9 - firstX, 8 - firstY, 9 - secondX, 8 - secondY);
                                //netClient.sendWin
                                winLostDialog.setVisible(true);
                                return;
                            } else {
                                // 播放将军的声音
                                if (res == 1) {
                                    AudioUtil.playKill();
                                }
                            }
                        } else {    // 吃子
                            int temp = map[firstX][firstY];
                            map[firstX][firstY] = -1;
                            int tempS = map[secondX][secondY];
                            map[secondX][secondY] = temp;
                            Chess c = chess[tempS];
                            chess[tempS] = null;
                            secondChess = chess[temp];
                            secondChess.setPos(secondX, secondY);
                            // 看一下这一步走后会不会造成将与被将的情况
                            int res = analyse();
                            if (res >= 2) {
                                map[firstX][firstY] = temp;
                                map[secondX][secondY] = tempS;
                                chess[tempS] = c;
                                chess[tempS].setPos(secondX, secondY);
                                chess[temp].setPos(firstX, firstY);
                                GameClient gameClient = GameClient.getInstance();
                                Opane.notice(gameClient, "将会被将，不允许");
                                return;
                            }

                            steps.add(new Step(temp, firstX, firstY, secondX, secondY, tempS));
//                            secondChess = chess[temp];
                            firstChess = null;
//                            secondChess.setPos(secondX, secondY);

                            // 播放落子声音
                            AudioUtil.playLuoZi();
                            if (isWin) {
                                // 弹出获胜界面
                                repaint();
                                GameClient gameClient = GameClient.getInstance();
                                netClient.sendWinCommand(gameClient.getUserId(), gameClient.getEnemyId(), 9 - firstX, 8 - firstY, 9 - secondX, 8 - secondY);
                                winLostDialog.setVisible(true);
                                return;
                            } else {
                                // 播放将军的声音
                                if (res == 1) {
                                    AudioUtil.playKill();
                                } else {
                                    // 吃
                                    AudioUtil.playEat();
                                }
                            }
                        }
                        repaint();
                        TimerUtil.timer.stop();
                        GameClient gameClient = GameClient.getInstance();
                        gameClient.getL1().setVisible(true);
                        gameClient.getL2().setVisible(false);
                        TimerUtil.timer = new Timer(1000, gameClient.getActionListener());
                        TimerUtil.timer.start();
                        netClient.sendRunCommand(gameClient.getEnemyId(), 9 - firstX, 8 - firstY, 9 - secondX, 8 - secondY);
                        isFirstClick = true;
                        isMyTurn.set(false);
                    }
                }
            }

            private Chess getChess(int x, int y) {
                int index_X = -1, index_Y = -1;
                for (int i = 0; i < 10; i ++) {
                    for (int j = 0; j < 9; j ++) {
                        Rectangle rectangle = new Rectangle(Position.LEFT_X + j * Position.Y_WIDTH, Position.LEFT_Y + i * Position.X_WIDTH, Position.X_WIDTH, Position.Y_WIDTH);
                        if (rectangle.contains(x, y)) {
                            index_X = i;
                            index_Y = j;
                            break;
                        }
                    }
                }

                if (index_X == -1) return null;

                if (map[index_X][index_Y] == -1) return new Chess(index_X, index_Y) {
                    @Override
                    public boolean isAllowMove(int[][] map, int x, int y) {
                        return false;
                    }
                };

                return chess[map[index_X][index_Y]];
            }
        });
    }

    public short getOwnerPlayer() {
        return ownerPlayer;
    }

    public AtomicBoolean getIsMyTurn() {
        return isMyTurn;
    }

    public boolean isMyTurn() {
        return isMyTurn.get();
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void move(int oldX, int oldY, int newX, int newY) {
//        boolean isLost = false;

//        if (ownerPlayer == PlayerType.RED_PLAYER && map[newX][newY] == 16) {
//            // 输了
//            isLost = true;
//            winLostDialog = new WinLostDialog(GameClient.getInstance(), "你输了！");
//        }
//
//        if (ownerPlayer == PlayerType.BLACK_PLAYER && map[newX][newY] == 0) {
//            // 输了
//            isLost = true;
//            winLostDialog = new WinLostDialog(GameClient.getInstance(), "你输了！");
//        }

        if (map[newX][newY] == -1) {    // 移动棋子
            int firstIndex = map[oldX][oldY];
            swap(oldX, oldY, newX, newY);
            secondChess = chess[map[newX][newY]];
            secondChess.setPos(newX, newY);
            firstChess = null;

            steps.add(new Step(firstIndex, oldX, oldY, newX, newY, -1));
            // 播放落子声音
            AudioUtil.playLuoZi();
//            if (isLost) {
//                // 输了的界面
//                repaint();
//                winLostDialog.setVisible(true);
//                return;
//            } else {
//                int res = analyse();
//                // 播放将军的声音
//                if (res >= 2) {
//                    AudioUtil.playKill();
//                }
//            }

            int res = analyse();
            // 播放将军的声音
            if (res >= 2) {
                AudioUtil.playKill();
            }
        } else {    // 吃子
            int temp = map[oldX][oldY];
            map[oldX][oldY] = -1;
            int tempS = map[newX][newY];
            map[newX][newY] = temp;
            chess[tempS] = null;
            secondChess = chess[temp];
            secondChess.setPos(newX, newY);
            firstChess = null;

            steps.add(new Step(temp, oldX, oldY, newX, newY, tempS));
            // 播放落子声音
            AudioUtil.playLuoZi();
//            if (isLost) {
//                repaint();
//                winLostDialog.setVisible(true);
//                return;
//            } else {
//                int res = analyse();
//
//                // 播放将军的声音
//                if (res >= 2) {
//                    AudioUtil.playKill();
//                } else {
//                    // 吃
//                    AudioUtil.playEat();
//                }
//            }

            int res = analyse();

            // 播放将军的声音
            if (res >= 2) {
                AudioUtil.playKill();
            } else {
                // 吃
                AudioUtil.playEat();
            }
        }
        repaint();
        TimerUtil.timer.stop();
        GameClient gameClient = GameClient.getInstance();
        gameClient.getL1().setVisible(false);
        gameClient.getL2().setVisible(true);
        TimerUtil.timer = new Timer(1000, gameClient.getActionListener());
        TimerUtil.timer.start();
        isMyTurn.set(true);
    }

    private void swap(int oldX, int oldY, int newX, int newY) {
        int temp = map[oldX][oldY];
        map[oldX][oldY] = -1;
        map[newX][newY] = temp;
    }

    /**
     * 判断棋局的将与被将的情况
     * @return 0 表示正常情况
     * 1 表示将军但未被将
     * 2 表示将军但被将
     * 3 表示纯粹被将
     */
    private int analyse() {
        int ownerGeneral, enemyGeneral;
        boolean isKilled = false, isKill = false;
        if (ownerPlayer == PlayerType.RED_PLAYER) {
            ownerGeneral = 16;
            enemyGeneral = 0;

            // 先判断是否会被将军
            for (int i = 0; i < 16; i ++) {
                if (chess[i] != null && chess[ownerGeneral] != null &&
                        chess[i].isAllowMove(map, chess[ownerGeneral].getX(), chess[ownerGeneral].getY())) {
                    isKilled = true;
                    break;
                }
            }

            // 判断是否会将军别人
            for (int i = 16; i < 32; i ++) {
                if (chess[i] != null && chess[enemyGeneral] != null &&
                        chess[i].isAllowMove(map, chess[enemyGeneral].getX(), chess[enemyGeneral].getY())) {
                    isKill = true;
                    break;
                }
            }
        } else {
            ownerGeneral = 0;
            enemyGeneral = 16;

            // 先判断是否会被将军
            for (int i = 16; i < 32; i ++) {
                if (chess[i] != null && chess[ownerGeneral] != null &&
                        chess[i].isAllowMove(map, chess[ownerGeneral].getX(), chess[ownerGeneral].getY())) {
                    isKilled = true;
                    break;
                }
            }

            // 判断是否会将军别人
            for (int i = 0; i < 16; i ++) {
                if (chess[i] != null && chess[enemyGeneral] != null &&
                        chess[i].isAllowMove(map, chess[enemyGeneral].getX(), chess[enemyGeneral].getY())) {
                    isKill = true;
                    break;
                }
            }
        }

        if (chess[ownerGeneral] != null && chess[enemyGeneral] != null) {
            if (chess[ownerGeneral].getY() == chess[enemyGeneral].getY()) {
                int indexY = chess[ownerGeneral].getY();
                int startX = Math.min(chess[ownerGeneral].getX(), chess[enemyGeneral].getX());
                int endX = Math.max(chess[ownerGeneral].getX(), chess[enemyGeneral].getX());
                boolean flag = true;
                for (int i = startX + 1; i < endX; i ++) {
                    if (map[i][indexY] != -1) {
                        flag = false;
                        break;
                    }
                }

                if (flag) isKilled = true;
            }
        }

        if (isKill && isKilled) return ResultType.KILL_AND_KILLED;
        if (isKill) return ResultType.KILL_AND_NOT_KILLED;
        if (isKilled) return ResultType.KILLED;
        return ResultType.NORMAL;
    }

    // 回退棋子
    public void rebackChess(int index, int x, int y, int oldX, int oldY) {
        chess[index].setPos(oldX, oldY);
        map[oldX][oldY] = index;
        map[x][y] = -1;
    }

    // 重新放置棋子
    public void resetChess(int index, int x, int y) {
        try {
            chess[index] = ChessFactory.getChess(index, x, y);
            map[x][y] = index;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void startNewGame(short player) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        steps = new ArrayList<>();
        initMap();
        initChess();

        ownerPlayer = player;
        isMyTurn = new AtomicBoolean(false);
        isMyTurn.set(ownerPlayer == PlayerType.RED_PLAYER);
        // 默认是红方视角，如果是黑方的话就倒转棋盘
        if (player == PlayerType.BLACK_PLAYER) {
            reverseBoard();
        }
        repaint();

        if (TimerUtil.timer != null) TimerUtil.timer.stop();
        TimerUtil.timer = new Timer(1000, GameClient.getInstance().getActionListener());
        TimerUtil.timer.start();
    }

    private void reverseBoard() {
        for (Chess ch : chess) {
            ch.reversePos();
        }

        for (int i = 0; i < 5; i ++) {
            for (int j = 0; j < 9; j ++) {
                int temp = map[9 - i][8 - j];
                map[9 - i][8 - j] = map[i][j];
                map[i][j] = temp;
            }
        }
    }

    private void initMap() {
        for (int i = 0; i < 10; i ++) {
            for (int j = 0; j < 9; j ++) {
                map[i][j] = - 1;
            }
        }
    }

    private void initChess() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < 32; i ++) {
            chess[i] = ChessFactory.getChess(i, Position.chessPosition[i][0], Position.chessPosition[i][1]);
            map[Position.chessPosition[i][0]][Position.chessPosition[i][1]] = i;
        }
    }

    // 绘制
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage(ChessBoard.class.getClassLoader().getResource("images/chessBoard.png"));
        g.drawImage(backgroundImage, 0, 0, 600, 600, this);

        for (int i = 0; i < 32; i ++) {
            if (chess[i] != null) {
                chess[i].paint(g, this);
            }
        }

        if (firstChess != null) {
            firstChess.drawSelectedChess(g);
        }
    }
}
