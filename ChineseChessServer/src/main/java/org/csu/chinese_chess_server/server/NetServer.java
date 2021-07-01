package org.csu.chinese_chess_server.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;
import org.csu.chinese_chess_server.constant.NetConfig;
import org.csu.chinese_chess_server.dto.CommandEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * NetServer
 *
 * @author zjx
 * @since 2021/6/11 9:04 下午
 */
@Component
public class NetServer {

    @Autowired
    private NetServerHandler netServerHandler;

    @Autowired
    private NetConfig netConfig;

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(8);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline channelPipeline = nioSocketChannel.pipeline();
                            channelPipeline.addLast(new LoggingHandler(String.valueOf(InternalLogLevel.INFO)));
                            channelPipeline.addLast(new CommandEncoder());
                            channelPipeline.addLast(netServerHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//            System.out.println(netConfig.SERVER_PORT);
            ChannelFuture f = b.bind(netConfig.SERVER_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @PostConstruct
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetServer.this.run();
            }
        }).start();
    }
}
