import net.NetClient;
import view.LoginView;

/**
 * 应用
 *
 * @author zjx
 * @since 2021/6/11 12:21 下午
 */
public class Application {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetClient netClient = NetClient.getNetClient();
                netClient.run();  // 连接服务器
            }
        }).start();

        new LoginView();
//         LoginView.getLoginView();
    }
}
