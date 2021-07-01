package net;

import chess.Step;
import constant.CommandType;
import constant.PlayerType;
import dto.Command;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.ThreadUtil;
import util.TimerUtil;
import view.*;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.*;

/**
 * 接收消息的处理器
 *
 * @author zjx
 * @since 2021/6/10 11:43 下午
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GameClient gameClient = GameClient.getInstance();
        Command command = (Command) msg;
        String commandType = command.getCommandType();
        switch (commandType) {
            case CommandType.COMMAND_CHOOSE_SUCCESS:
                ChoosingDialog choosingDialog = gameClient.getChoosingDialog();
                choosingDialog.closeDialog();
//                gameClient.setChoosingDialog(null);
                gameClient.setEnemyId(command.getEnemyId());
                gameClient.setEnemyName(command.getEnemyName());
                try {
                    gameClient.getButtonStart().setVisible(false);
                    gameClient.getButtonGiveIn().setEnabled(true);
                    gameClient.getButtonAskRegret().setEnabled(true);
                    short player = command.getPlayer();
                    if (player == PlayerType.RED_PLAYER) {
                        gameClient.getL2().setVisible(true);
                        gameClient.getL1().setVisible(false);
                    } else {
                        gameClient.getL1().setVisible(true);
                        gameClient.getL2().setVisible(false);
                    }
                    gameClient.getChessBoard().startNewGame(command.getPlayer());
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                } catch (NoSuchMethodException noSuchMethodException) {
                    noSuchMethodException.printStackTrace();
                } catch (InstantiationException instantiationException) {
                    instantiationException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
                break;
            case CommandType.COMMAND_CHOOSE_FAIL:
                ChoosingDialog choosingDialog1 = gameClient.getChoosingDialog();
                choosingDialog1.closeDialog();
                Opane.notice(gameClient, "暂时没找到匹配对手");
                break;
            case CommandType.COMMAND_RUN:
                gameClient.getChessBoard().move(command.getOldX(), command.getOldY(), command.getNewX(), command.getNewY());
                break;
            case CommandType.COMMAND_OTHER_CONNECT_FAIL:
                System.out.println("5555555555");
                gameClient.getButtonStart().setVisible(true);
                gameClient.getButtonGiveIn().setEnabled(false);
                gameClient.getButtonAskRegret().setEnabled(false);
                Opane.notice(gameClient, "对手离开游戏");
                gameClient.getIsOtherClosed().set(true);
                break;
            case CommandType.COMMAND_REGRET:
                NetClient netClient = NetClient.getNetClient();

                int choice = Opane.regretIsOk(gameClient);
                if (choice == 1) {  // 拒绝悔棋
                    netClient.sendRegretFail(gameClient.getEnemyId());
                } else if (choice == 0) {   // 同意悔棋
                    netClient.sendRegretSuccess(gameClient.getEnemyId());

                    gameClient.getChessBoard().getIsMyTurn().set(false);
                    ArrayList<Step> steps = gameClient.getChessBoard().getSteps();

                    Step step = steps.get(steps.size() - 1);
                    short player = step.getIndex() > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER;
                    steps.remove(steps.size() - 1);
                    gameClient.getChessBoard().rebackChess(step.getIndex(), step.getNewX(), step.getNewY(), step.getOldX(), step.getOldY());
                    if (step.getEatIndex() != -1) {
                        gameClient.getChessBoard().resetChess(step.getEatIndex(), step.getNewX(), step.getNewY());
                    }

                    while (player == gameClient.getChessBoard().getOwnerPlayer()) {
                        step = steps.get(steps.size() - 1);
                        player = step.getIndex() > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER;
                        steps.remove(steps.size() - 1);
                        gameClient.getChessBoard().rebackChess(step.getIndex(), step.getNewX(), step.getNewY(), step.getOldX(), step.getOldY());
                        if (step.getEatIndex() != -1) {
                            gameClient.getChessBoard().resetChess(step.getEatIndex(), step.getNewX(), step.getNewY());
                        }
                    }

                    gameClient.getChessBoard().repaint();
                    TimerUtil.timer.stop();
                    gameClient.getL1().setVisible(true);
                    gameClient.getL2().setVisible(false);
                    TimerUtil.timer = new Timer(1000, gameClient.getActionListener());
                    TimerUtil.timer.start();
                }
                break;
            case CommandType.COMMAND_REGRET_SUCCESS:
                gameClient.getWaitingDialog().closeDialog();
                JOptionPane.showMessageDialog(gameClient, "对方同意了你的悔棋请求");

                ArrayList<Step> steps = gameClient.getChessBoard().getSteps();

                Step step = steps.get(steps.size() - 1);
                short player = step.getIndex() > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER;
                steps.remove(steps.size() - 1);
                gameClient.getChessBoard().rebackChess(step.getIndex(), step.getNewX(), step.getNewY(), step.getOldX(), step.getOldY());
                if (step.getEatIndex() != -1) {
                    gameClient.getChessBoard().resetChess(step.getEatIndex(), step.getNewX(), step.getNewY());
                }

                while (player != gameClient.getChessBoard().getOwnerPlayer()) {
                    step = steps.get(steps.size() - 1);
                    player = step.getIndex() > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER;
                    steps.remove(steps.size() - 1);
                    gameClient.getChessBoard().rebackChess(step.getIndex(), step.getNewX(), step.getNewY(), step.getOldX(), step.getOldY());
                    if (step.getEatIndex() != -1) {
                        gameClient.getChessBoard().resetChess(step.getEatIndex(), step.getNewX(), step.getNewY());
                    }
                }
                gameClient.getChessBoard().getIsMyTurn().set(true);
                gameClient.getChessBoard().repaint();
                TimerUtil.timer.stop();
                gameClient.getL1().setVisible(false);
                gameClient.getL2().setVisible(true);
                TimerUtil.timer = new Timer(1000, gameClient.getActionListener());
                TimerUtil.timer.start();
                break;
            case CommandType.COMMAND_REGRET_FAIL:
                gameClient.getWaitingDialog().closeDialog();
                JOptionPane.showMessageDialog(gameClient, "对方拒绝了你的悔棋请求");
                break;
            case CommandType.COMMAND_GIVE_IN:
                ThreadUtil.threadPoolExecutor.execute(() -> {
                    WinLostDialog winLostDialog = new WinLostDialog(GameClient.getInstance(), "恭喜你，获胜！");
                    winLostDialog.showDialog();
                });
                break;
            case CommandType.COMMAND_WIN:
                gameClient.getChessBoard().move(command.getOldX(), command.getOldY(), command.getNewX(), command.getNewY());
                ThreadUtil.threadPoolExecutor.execute(() -> {
                    WinLostDialog winLostDialog = new WinLostDialog(GameClient.getInstance(), "你输了！");
                    winLostDialog.showDialog();
                });
                break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NetClient netClient = NetClient.getNetClient();
        GameClient gameClient = GameClient.getInstance();
        netClient.sendRemoteDisConnect(gameClient.getUserId(), gameClient.getEnemyId());
        WinLostDialog winLostDialog = new WinLostDialog(gameClient, "你网络断开连接");
        winLostDialog.setVisible(true);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
