package org.csu.chinese_chess_server.server;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/15 上午11:12
 */
public class SessionHolder {

    public static final Lock lock = new ReentrantLock();

    public static final Map<Integer, Channel> mp = new ConcurrentHashMap<>();

    public static final Map<Integer, Boolean> IsStart = new ConcurrentHashMap<>();

    public static final BlockingQueue<Integer> q = new LinkedBlockingQueue<>(500);
}
