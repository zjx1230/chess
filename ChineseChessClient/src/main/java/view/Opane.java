package view;

import javax.swing.*;
import java.awt.*;

/**
 * 弹窗类
 *
 * @author zjx
 * @since 2021/6/11 11:06 上午
 */
public class Opane {

    public static void notice(Component component, String message) {
        JOptionPane.showMessageDialog(
                component,
                message,
                "通知",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void registerOK(LoginView loginView) {
        JOptionPane.showMessageDialog(
                loginView,
                "注册成功",
                "提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void registerWrong(RegisterView registerView) {
        JOptionPane.showMessageDialog(
                registerView,
                "两次密码不一致，请重新确认",
                "提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void registerFail(RegisterView registerView) {
        JOptionPane.showMessageDialog(
                registerView,
                "后台注册失败",
                "提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void loginOK(LoginView loginView) {
        JOptionPane.showMessageDialog(
                loginView,
                "登录成功",
                "提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void loginFail(LoginView loginView) {
        JOptionPane.showMessageDialog(
                loginView,
                "登录失败，请检查用户名和密码是否正确",
                "提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static int regretIsOk(Component component) {
        String msg = "对方请求悔棋，是否同意？";
        int type = JOptionPane.YES_NO_OPTION;
        String title = "请求悔棋";
        return JOptionPane.showConfirmDialog(component, msg, title, type);
    }
}
