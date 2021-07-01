package view;

import net.NetClient;
import util.ThreadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 输了赢了界面
 *
 * @author zjx
 * @since 2021/6/17 上午9:34
 */
public class WinLostDialog extends JDialog {

    private JButton restartBtn;

//    @Override
//    public void setBounds(int x, int y, int width, int height) {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Dimension screen = tk.getScreenSize();
//        super.setBounds(screen.width / 2,
//                screen.height / 2, 200, 100);
//    }

    public WinLostDialog(JFrame owner, String message) {
        super(owner, "通知", true);
        restartBtn = new JButton("重新开始游戏");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(new JLabel(message), BorderLayout.CENTER);
        jPanel.add(restartBtn, BorderLayout.SOUTH);
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource("images/chess2.png")));
        add(jPanel);

        setSize(200, 100);
        int windowWidth = this.getWidth(); // 获得窗口宽
        int windowHeight = this.getHeight(); // 获得窗口高
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽
        int screenHeight = kit.getScreenSize().height; // 获取屏幕的高
        this.setLocation(screenWidth/2 - windowWidth/2, screenHeight/2 - windowHeight/2); // 设置窗口居中显示

        // 添加按钮事件
        restartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GameClient gameClient = GameClient.getInstance();
                gameClient.getIsOtherClosed().set(false);
                ThreadUtil.threadPoolExecutor.execute(() -> {
                    NetClient netClient = NetClient.getNetClient();
                    netClient.sendChooseCommand(gameClient.getUserId());
                });

                dispose();
                gameClient.getChoosingDialog().showDialog();
            }
        });
    }

    public void showDialog() {
        setVisible(true);
    }
}
