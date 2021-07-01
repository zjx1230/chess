package view;

import constant.CommandType;
import listener.BtnListener;
import validate.TxDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 注册界面
 *
 * @author zjx
 * @since 2021/6/11 10:32 上午
 */
public class RegisterView extends JFrame {

    private JLabel label1 = new JLabel("用户名");

    private JTextField userName = new JTextField(10);

    private JLabel label2 = new JLabel("密 码");

    private JPasswordField password = new JPasswordField(10);

    private JPasswordField confirmPassword;

    public RegisterView(LoginView loginView) {
        setSize(350, 200);
        initComponent(loginView);
        setBounds(new Rectangle(
                (int) loginView.getBounds().getX(),
                (int) loginView.getBounds().getY(),
                (int) loginView.getBounds().getWidth(),
                (int) loginView.getBounds().getHeight()
        ));
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

    public String getConfirmPassword() {
        return new String(confirmPassword.getPassword());
    }

    public JTextField getUserName() {
        return userName;
    }

    public JPasswordField getPassword() {
        return password;
    }

    private void initComponent(LoginView loginView) {
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(userName);

        JPanel panel2 = new JPanel();
        panel2.add(label2);
        panel2.add(password);

        JPanel panel3 = new JPanel();
        JLabel label3 = new JLabel("确认密码");
        confirmPassword = new JPasswordField(8);
        confirmPassword.setDocument(new TxDocument(15));
        JButton registerBtn = new JButton("注册");
        registerBtn.addActionListener(BtnListener.getBtnListener());
        panel3.add(label3); panel3.add(confirmPassword);

        JPanel panel4 = new JPanel();
        registerBtn.setActionCommand(CommandType.COMMAND_REGISTER);
        panel4.add(registerBtn);

        Box vBox = Box.createVerticalBox();
        vBox.add(panel1);
        vBox.add(panel2);
        vBox.add(panel3);
        vBox.add(panel4);

        setContentPane(vBox);
    }
}
