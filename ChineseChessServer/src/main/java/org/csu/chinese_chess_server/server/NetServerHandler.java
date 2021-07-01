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
        System.out.println(Thread.currentThread());
        Command command = (Command) msg;
        String commandType = command.getCommandType();
        switch (commandType) {
            case CommandType.COMMAND_REMOTE_CONNECT:    // 新客户端连接注册
                SessionHolder.mp.put(command.getUserId(), channelHandlerContext.channel());
                break;
            case CommandType.COMMAND_CHOOSE:            // 客户端匹配游戏对手
                SessionHolder.q.offer(command.getUserId());
                SessionHolder.lock.lock();
                if (SessionHolder.IsStart.getOrDefault(command.getUserId(), false)) {
                    SessionHolder.lock.unlock();
                    break;
                }
                Integer enemyId = SessionHolder.q.poll(5, TimeUnit.SECONDS);

                if (command.getUserId().equals(enemyId)) {
                    enemyId = SessionHolder.q.poll(5, TimeUnit.SECONDS);
                }

                SessionHolder.q.remove(command.getUserId());

                if (enemyId == null || enemyId.equals(command.getUserId())) {
                    SessionHolder.IsStart.put(command.getUserId(), false);
                    SessionHolder.lock.unlock();
                    Command command1 = new Command();
                    command1.setCommandType(CommandType.COMMAND_CHOOSE_FAIL);
                    channelHandlerContext.channel().writeAndFlush(command1);
                } else {
                    SessionHolder.IsStart.put(command.getUserId(), true);
                    SessionHolder.IsStart.put(enemyId, true);
                    SessionHolder.lock.unlock();
                    Command command1 = new Command();
                    Command command2 = new Command();
                    command1.setEnemyId(enemyId);
                    command1.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);
                    command2.setEnemyId(command.getUserId());
                    command2.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);
                    if (UserHelper.userMap.containsKey(enemyId)) {
                        command1.setEnemyName(UserHelper.userMap.get(enemyId));
                    } else {
                        User user = userService.getUser(enemyId);
                        UserHelper.userMap.put(enemyId, user.getUserName());
                        command1.setEnemyName(user.getUserName());
                    }

                    if (UserHelper.userMap.containsKey(command.getUserId())) {
                        command2.setEnemyName(UserHelper.userMap.get(command.getUserId()));
                    } else {
                        User user = userService.getUser(command.getUserId());
                        UserHelper.userMap.put(command.getUserId(), user.getUserName());
                        command2.setEnemyName(user.getUserName());
                    }

                    int player = ThreadLocalRandom.current().nextInt(2);
                    command1.setPlayer((short) player);
                    command2.setPlayer((short) (1 - player));
                    channelHandlerContext.channel().writeAndFlush(command1);
                    SessionHolder.mp.get(enemyId).writeAndFlush(command2);
                }
                break;
            case CommandType.COMMAND_RUN:
                SessionHolder.mp.get(command.getEnemyId()).writeAndFlush(command);
                break;
            case CommandType.COMMAND_REMOTE_DISCONNECT:
                Command command1 = new Command();
                command1.setCommandType(CommandType.COMMAND_OTHER_CONNECT_FAIL);
                mp.get(command.getEnemyId()).writeAndFlush(command1).sync();
                lock.lock();
                q.remove(command.getUserId());
                q.remove(command.getEnemyId());
                mp.remove(command.getUserId());
                IsStart.remove(command.getUserId());
                IsStart.put(command.getEnemyId(), false);
                lock.unlock();
                break;
            case CommandType.COMMAND_REGRET:
            case CommandType.COMMAND_REGRET_SUCCESS:
            case CommandType.COMMAND_REGRET_FAIL:
                mp.get(command.getEnemyId()).writeAndFlush(command).sync();
                break;
            case CommandType.COMMAND_GIVE_IN:
            case CommandType.COMMAND_WIN:
                lock.lock();
                q.remove(command.getUserId());
                q.remove(command.getEnemyId());
                IsStart.remove(command.getUserId());
                IsStart.put(command.getEnemyId(), false);
                lock.unlock();
                mp.get(command.getEnemyId()).writeAndFlush(command).sync();
                break;
        }
    }
}
