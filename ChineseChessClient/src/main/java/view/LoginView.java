package view;

import constant.ChessType;
import constant.CommandType;
import listener.BtnListener;
import net.NetClient;
import validate.TxDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 登录界面
 *
 * @author zjx
 * @since 2021/6/11 9:49 上午
 */
public class LoginView extends JFrame {

    private JLabel label1 = new JLabel("用户名");

    private JTextField userName = new JTextField(10);

    private JLabel label2 = new JLabel("密 码");

    private JPasswordField password = new JPasswordField(10);

    private JButton loginBtn = new JButton("登录");

    private JButton regesiterBtn = new JButton("注册");

//    public static LoginView loginView = new LoginView();

//    public static LoginView getLoginView() {
//        return loginView;
//    }

    public LoginView() {
        // 设置窗口大小
        setSize(350, 200);
        // 设置按下右上角X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 初始化组件
        initComponent();
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource("images/chess2.png")));
        setResizable(false);
        setVisible(true);

        addWindowListener(new WindowAdapter() {    // 窗口关闭事件
            @Override
            public void windowClosing(WindowEvent e){
                try {
                    System.exit(0);
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initComponent() {
        // 定义面板封装文本框和标签
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userName.setDocument(new TxDocument(15));
        panel1.add(label1); panel1.add(userName);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        password.setDocument(new TxDocument(15));
        panel2.add(label2); panel2.add(password);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginBtn.setActionCommand(CommandType.COMMAND_LOGIN);
        BtnListener btnListener = BtnListener.getBtnListener();
        btnListener.setLoginView(this);
        loginBtn.addActionListener(btnListener);
        regesiterBtn.setActionCommand(CommandType.COMMAND_REGISTER_LOGIN);
        regesiterBtn.addActionListener(BtnListener.getBtnListener());
        panel3.add(loginBtn); panel3.add(regesiterBtn);

        Box vbox = Box.createVerticalBox();
        vbox.add(panel1);
        vbox.add(panel2);
        vbox.add(panel3);

        setContentPane(vbox);
    }

    public JLabel getLabel1() {
        return label1;
    }

    public JLabel getLabel2() {
        return label2;
    }

    public JTextField getUserName() {
        return userName;
    }

    public JPasswordField getPassword() {
        return password;
    }
}
