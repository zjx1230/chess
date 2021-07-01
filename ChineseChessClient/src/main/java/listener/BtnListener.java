package listener;

import constant.CommandType;
import lombok.SneakyThrows;
import util.HttpUtil;
import net.NetClient;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import view.GameClient;
import view.LoginView;
import view.Opane;
import view.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 按钮监听类
 *
 * @author zjx
 * @since 2021/6/11 11:16 上午
 */
public class BtnListener implements ActionListener {

    private final static BtnListener btnListener = new BtnListener();

    private LoginView loginView;

    private RegisterView registerView;

    private GameClient gameClient;

    private BtnListener() {}

    public static final BtnListener getBtnListener() {
        return btnListener;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // 按下登录按钮
        if (CommandType.COMMAND_LOGIN.equals(command)) {
            // 发送登录请求
            Map<String, String> param = new HashMap<>();
            param.put("userName", loginView.getUserName().getText());
            param.put("password", new String(loginView.getPassword().getPassword()));
            HttpEntity httpEntity = HttpUtil.sendPost("user/login", param);
            String userId = EntityUtils.toString(httpEntity);
            if (userId.equals("-1")) {
                Opane.loginFail(loginView);
            } else {
                if (registerView != null) registerView.dispose();
                loginView.dispose();
                gameClient = GameClient.getInstance();
                gameClient.setUserId(Integer.parseInt(userId));
                NetClient netClient = NetClient.getNetClient();
                netClient.sendRegisterCommand(Integer.parseInt(userId));
            }
        } else if (CommandType.COMMAND_REGISTER.equals(command)) {
//            System.out.println(registerView.getConfirmPassword());
//            System.out.println(new String(registerView.getPassword().getPassword()));
            if (!registerView.getConfirmPassword().equals(new String(registerView.getPassword().getPassword()))) {
                Opane.registerWrong(registerView);
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("userName", registerView.getUserName().getText());
            param.put("password", new String(registerView.getPassword().getPassword()));
            HttpEntity httpEntity = HttpUtil.sendPost("user/register", param);
            String response = EntityUtils.toString(httpEntity);

            if (response.equals("success")) {
                registerView.setVisible(false);
                loginView.setVisible(true);
                Opane.registerOK(loginView);
            } else {
                Opane.registerFail(registerView);
//                Opane.registerWrong(registerView);
            }
        } else if (CommandType.COMMAND_REGISTER_LOGIN.equals(command)) {
            registerView = new RegisterView(loginView);
            loginView.setVisible(false);
        }
    }
}
