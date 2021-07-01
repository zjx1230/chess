package view;

import util.ThreadUtil;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 显示匹配对手中的提示
 *
 * @author zjx
 * @since 2021/6/14 上午10:45
 */
public class ChoosingDialog extends JDialog {

//    @Override
//    public void setBounds(int x, int y, int width, int height) {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Dimension screen = tk.getScreenSize();
////        GameClient parent = GameClient.getInstance();
//        super.setBounds(screen.width / 2,
//                screen.height / 2, 200, 100);
//    }

    public ChoosingDialog(JFrame owner) {
        super(owner, "通知", true);
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource("images/chess2.png")));
        add(new JLabel("正在匹配对手中,请稍等"), BorderLayout.CENTER);
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

    public static void main(String[] args) throws InterruptedException {
        ChoosingDialog choosingDialog = new ChoosingDialog(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                choosingDialog.showDialog();
            }
        }).start();
        Thread.sleep(5000);
        choosingDialog.closeDialog();
        Thread.sleep(5000);
        choosingDialog.showDialog();

        while (true);
    }
}
