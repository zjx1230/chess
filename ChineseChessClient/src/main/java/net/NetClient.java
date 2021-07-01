package net;

import com.alibaba.fastjson.JSON;
import constant.CommandType;
import constant.NetConfig;
import dto.Command;
import dto.CommandEncoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import view.ChessBoard;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import view.GameClient;

import java.net.InetSocketAddress;

import static constant.NetConfig.SERVER_ADDR;

/**
 * Netty 客户端
 *
 * @author zjx
 * @since 2021/6/10 11:57 下午
 */
public class NetClient {

    private final static NetClient netClient = new NetClient();

    private Channel channel;

    private NetClient() {}

    public static NetClient getNetClient() {
        return netClient;
    }

    public void run() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            channelPipeline.addLast(new CommandEncoder());
                            channelPipeline.addLast(new ClientHandler());
                        }
                    })
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_KEEPALIVE, true);
            channel = bootstrap.connect(NetConfig.SERVER_IP, NetConfig.SERVER_PORT).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    // 将userId和对应Socket注册到服务器
    public void sendRegisterCommand(int userId) {
        Command command = new Command();
        command.setUserId(userId);
        command.setCommandType(CommandType.COMMAND_REMOTE_CONNECT);
        send(command);
    }

    // 进行对手匹配
    public void sendChooseCommand(int userId) {
        Command command = new Command();
        command.setUserId(userId);
        command.setCommandType(CommandType.COMMAND_CHOOSE);
        send(command);
    }

    public void sendRunCommand(int enemyId, int oldX, int oldY, int newX, int newY) {
        Command command = new Command();
        command.setCommandType(CommandType.COMMAND_RUN);
        command.setEnemyId(enemyId);
        command.setOldX(oldX);
        command.setOldY(oldY);
        command.setNewX(newX);
        command.setNewY(newY);

        send(command);
    }

    public void sendWinCommand(int userId, int enemyId, int oldX, int oldY, int newX, int newY) {
        Command command = new Command();
        command.setCommandType(CommandType.COMMAND_WIN);
        command.setEnemyId(enemyId);
        command.setUserId(userId);
        command.setOldX(oldX);
        command.setOldY(oldY);
        command.setNewX(newX);
        command.setNewY(newY);

        send(command);
    }

    public void sendRemoteDisConnect(int userId, int enemyId) {
        Command command = new Command();
        command.setUserId(userId);
        command.setEnemyId(enemyId);
        command.setCommandType(CommandType.COMMAND_REMOTE_DISCONNECT);

        send(command);
    }

    public void sendRegret(int enemyId) {
        Command command = new Command();
        command.setEnemyId(enemyId);
        command.setCommandType(CommandType.COMMAND_REGRET);

        send(command);
    }

    public void sendRegretSuccess(int enemyId) {
        Command command = new Command();
        command.setEnemyId(enemyId);
        command.setCommandType(CommandType.COMMAND_REGRET_SUCCESS);

        send(command);
    }

    public void sendRegretFail(int enemyId) {
        Command command = new Command();
        command.setEnemyId(enemyId);
        command.setCommandType(CommandType.COMMAND_REGRET_FAIL);

        send(command);
    }

    public void sendGiveIn(int userId, int enemyId) {
        Command command = new Command();
        command.setUserId(userId);
        command.setEnemyId(enemyId);
        command.setCommandType(CommandType.COMMAND_GIVE_IN);

        send(command);
    }

    private void send(Command command) {
        try {
            channel.writeAndFlush(command).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
