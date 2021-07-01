package util;

import java.util.concurrent.*;

/**
 * 多线程并发相关
 *
 * @author zjx
 * @since 2021/6/19 上午11:33
 */
public class ThreadUtil {

    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(8));

}
