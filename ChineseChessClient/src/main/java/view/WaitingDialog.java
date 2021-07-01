package view;

import javax.swing.*;
import java.awt.*;

/**
 * 等待对方回应
 *
 * @author zjx
 * @since 2021/6/18 下午5:56
 */
public class WaitingDialog extends JDialog {

//    @Override
//    public void setBounds(int x, int y, int width, int height) {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Dimension screen = tk.getScreenSize();
////        GameClient parent = GameClient.getInstance();
//        super.setBounds(screen.width / 2,
//                screen.height / 2, 200, 100);
//    }

    public WaitingDialog(JFrame owner) {
        super(owner, "提示", true);
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource("images/chess2.png")));
        add(new JLabel("正在等待对手回应"), BorderLayout.CENTER);
        setSize(200, 100);
        int windowWidth = this.getWidth(); // 获得窗口宽
        int windowHeight = this.getHeight(); // 获得窗口高
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽
        int screenHeight = kit.getScreenSize().height; // 获取屏幕的高
        this.setLocation(screenWidth/2 - windowWidth/2, screenHeight/2 - windowHeight/2); // 设置窗口居中显示
    }

    public void showDialog() {
        setVisible(true);
    }

    public void closeDialog() {
        this.dispose();
    }
}
