package org.csu.chinese_chess_server.server;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import org.csu.chinese_chess_server.constant.CommandType;
import org.csu.chinese_chess_server.constant.UserHelper;
import org.csu.chinese_chess_server.dto.Command;
import org.csu.chinese_chess_server.pojo.User;
import org.csu.chinese_chess_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.*;

import static org.csu.chinese_chess_server.server.SessionHolder.*;

/**
 * NetServerHandler
 *
 * @author zjx
 * @since 2021/6/11 10:01 下午
 */
@Component
@ChannelHandler.Sharable
public class NetServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UserService userService;

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        System.out.println(Thread.currentThread());
        Command command = (Command) msg;
        String commandType = command.getCommandType();
        switch (commandType) {
            case CommandType.COMMAND_REMOTE_CONNECT:    // 新客户端连接注册
                SessionHolder.mp.put(command.getUserId(), channelHandlerContext.channel());
                break;
            case CommandType.COMMAND_CHOOSE:            // 客户端匹配游戏对手
                Integer enemyId;
                synchronized (SessionHolder.class) {
                    if (isStart.contains(command.getUserId())) break;
                    enemyId = SessionHolder.q.poll();
                    if (enemyId == null || command.getUserId().equals(enemyId)) {
                        SessionHolder.q.offer(command.getUserId());
                    } else {
                        SessionHolder.q.remove(command.getUserId());
                        isStart.add(command.getUserId());
                        isStart.add(enemyId);
                    }
                }

                if (enemyId == null || enemyId.equals(command.getUserId())) {
                    Command command1 = new Command();
                    command1.setCommandType(CommandType.COMMAND_CHOOSE_FAIL);
                    Thread.sleep(3000);

                    synchronized (SessionHolder.class) {
                        if (isStart.contains(command.getUserId())) break;
                        channelHandlerContext.channel().writeAndFlush(command1);
                    }
                    break;
                }

                Command command1 = new Command();
                Command command2 = new Command();
                command1.setEnemyId(enemyId);
                command1.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);
                command2.setEnemyId(command.getUserId());
                command2.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);

                String enemyName1 = UserHelper.userMap.get(enemyId);
                if (enemyName1 != null) {
                    command1.setEnemyName(enemyName1);
                } else {
                    User user = userService.getUser(enemyId);
                    UserHelper.userMap.put(enemyId, user.getUserName());
                    command1.setEnemyName(user.getUserName());
                }

                String enemyName2 = UserHelper.userMap.get(command.getUserId());
                if (enemyName2 != null) {
                    command2.setEnemyName(enemyName2);
                } else {
                    User user = userService.getUser(command.getUserId());
                    UserHelper.userMap.put(command.getUserId(), user.getUserName());
                    command2.setEnemyName(user.getUserName());
                }

                Random random = new Random();
                int player = random.nextInt(2);
                command1.setPlayer((short) player);
                command2.setPlayer((short) (1 - player));
                channelHandlerContext.channel().writeAndFlush(command1);
                SessionHolder.mp.get(enemyId).writeAndFlush(command2);
                break;
            case CommandType.COMMAND_RUN:
                SessionHolder.mp.get(command.getEnemyId()).writeAndFlush(command);
                break;
            case CommandType.COMMAND_REMOTE_DISCONNECT:
                Command command3 = new Command();
                command3.setCommandType(CommandType.COMMAND_OTHER_CONNECT_FAIL);
                synchronized (SessionHolder.class) {
                    isStart.remove(command.getUserId());
                    isStart.remove(command.getEnemyId());
                }
                mp.get(command.getEnemyId()).writeAndFlush(command3).sync();
                break;
            case CommandType.COMMAND_REGRET:
            case CommandType.COMMAND_REGRET_SUCCESS:
            case CommandType.COMMAND_REGRET_FAIL:
                mp.get(command.getEnemyId()).writeAndFlush(command).sync();
                break;
            case CommandType.COMMAND_GIVE_IN:
            case CommandType.COMMAND_WIN:
                synchronized (SessionHolder.class) {
                    isStart.remove(command.getUserId());
                    isStart.remove(command.getEnemyId());
                }
                mp.get(command.getEnemyId()).writeAndFlush(command).sync();
                break;
        }
    }
}
