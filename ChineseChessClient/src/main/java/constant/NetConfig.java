package constant;

import java.net.InetSocketAddress;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/14 上午10:54
 */
public class NetConfig {

    public static final String SERVER_IP = "1.15.157.198";

//    public static final String SERVER_IP = "127.0.0.1";

    public static final int SERVER_PORT = 6666;

    public static final InetSocketAddress SERVER_ADDR = new InetSocketAddress(
            NetConfig.SERVER_IP, NetConfig.SERVER_PORT);
}
