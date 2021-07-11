package org.csu.chinese_chess_server.server;

import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于游戏匹配
 *
 * @author zjx
 * @since 2021/6/15 上午11:12
 */
public class SessionHolder {

    public static final Map<Integer, Channel> mp = new ConcurrentHashMap<>();

    public static final Set<Integer> isStart = new HashSet<>();

    public static final Queue<Integer> q = new LinkedList<>();
}
