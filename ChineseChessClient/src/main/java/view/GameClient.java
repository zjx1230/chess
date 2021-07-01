package view;

import constant.CommandType;
import constant.PlayerType;
import dto.Command;
import net.NetClient;
import util.ThreadUtil;
import util.TimerUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 下棋主界面
 *
 * @author zjx
 * @since 2021/6/8 11:05 下午
 */
public class GameClient extends JFrame {

    private JPanel headPanel = new JPanel();

    private JPanel bottomPanel = new JPanel();

    private ChessBoard chessBoard = new ChessBoard();

    private JButton buttonGiveIn = new JButton("认输");

    private JButton buttonStart = new JButton("开始游戏");

    private JButton buttonAskRegret = new JButton("请求悔棋");

    private JButton l1 = new JButton("60");  //  计时

    private JButton l2 = new JButton("60");

    private ChoosingDialog choosingDialog;

    private WaitingDialog waitingDialog;

    private Integer userId;

    private Integer enemyId;

    private JLabel enemyName = new JLabel("对手");

    private volatile static GameClient gameClient;

    private AtomicBoolean isOtherClosed = new AtomicBoolean(false);

    public static GameClient getInstance() {
        if (gameClient == null) {
            synchronized (GameClient.class) {
                if (gameClient == null) {
                    gameClient = new GameClient();
                }
            }
        }

        return gameClient;
    }

    public AtomicBoolean getIsOtherClosed() {
        return isOtherClosed;
    }

    public void setIsOtherClosed(AtomicBoolean isOtherClosed) {
        this.isOtherClosed = isOtherClosed;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setEnemyId(Integer enemyId) {
        this.enemyId = enemyId;
    }

    public Integer getEnemyId() {
        return enemyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName.setText(enemyName);
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public JButton getL1() {
        return l1;
    }

    public JButton getL2() {
        return l2;
    }

    public ChoosingDialog getChoosingDialog() {
        return choosingDialog;
    }

    public void setChoosingDialog(ChoosingDialog choosingDialog) {
        this.choosingDialog = choosingDialog;
    }

    public WaitingDialog getWaitingDialog() {
        return waitingDialog;
    }

    public JButton getButtonStart() {
        return buttonStart;
    }

    public JButton getButtonGiveIn() {
        return buttonGiveIn;
    }

    public JButton getButtonAskRegret() {
        return buttonAskRegret;
    }

    public ActionListener getActionListener() {
        return new AbstractAction() {

            private int i = 60;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (l1.isVisible()) {
                    l1.setText(String.valueOf(i --));
                    if (i < 10) {
                        l1.setForeground(Color.RED);
                        l1.revalidate();
                    } else {
                        l1.setForeground(Color.BLACK);
                        l1.revalidate();
                    }

                    if (i == 0)  {
                        chessBoard.getIsMyTurn().set(true);
                        i = 60;
                        l2.setVisible(true);
                        l1.setVisible(false);
                    }
                } else if (l2.isVisible()) {
                    l2.setText(String.valueOf(i --));
                    if (i < 10) {
                        l2.setForeground(Color.RED);
                        l2.revalidate();
                    } else {
                        l2.setForeground(Color.BLACK);
                        l2.revalidate();
                    }

                    if (i == 0)  {
                        chessBoard.getIsMyTurn().set(false);
                        i = 60;
                        l1.setVisible(true);
                        l2.setVisible(false);
                    }
                }

                repaint();
            }
        };
    }

    private GameClient() {
        // 设置总体整个窗口
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screen = tk.getScreenSize();
        setLocation(screen.width / 2 - 300, screen.height / 2 - 200);

        // 头部
        headPanel.setSize(this.getWidth(), 500);
        headPanel.setLayout(new FlowLayout());
        JButton button = new JButton("");
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(50, 50));
        button.setEnabled(false);
        button.setBorderPainted(false); //不打印边框
        button.setBorder(null); //除去边框
        button.setFocusPainted(false); //除去焦点的框
        button.setContentAreaFilled(false); //除去默认的背景填充
        headPanel.add(button);

        enemyName.setFont(new Font("黑体", Font.PLAIN, 15));
        headPanel.add(enemyName);

        // 右边
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 1, 0, 100));
        buttonStart.setPreferredSize(new Dimension(130, 10));
        l1.setBorderPainted(false); l2.setBorderPainted(false);
        l1.setEnabled(false); l2.setEnabled(false);
        l1.setVisible(false); l2.setVisible(false);
        l1.setFont(new Font("隶书", Font.BOLD, 15));
        l2.setFont(new Font("隶书", Font.BOLD, 15));
        btnPanel.add(l1);
        btnPanel.add(buttonGiveIn);
        btnPanel.add(buttonAskRegret);
        btnPanel.add(buttonStart);
        btnPanel.add(l2);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(headPanel, BorderLayout.NORTH);
        this.getContentPane().add(chessBoard,BorderLayout.CENTER);
        this.getContentPane().add(btnPanel,BorderLayout.EAST);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        this.setSize(720,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("中国象棋");
        this.setResizable(false);
        this.setVisible(true);
        buttonGiveIn.setEnabled(false);
        buttonAskRegret.setEnabled(false);
        buttonStart.setEnabled(true);
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(this.getClass().getClassLoader().getResource("images/chess2.png")));
        setVisible(true);
        choosingDialog = new ChoosingDialog(null);
//        choosingDialog.setUndecorated(true);

        addWindowListener(new WindowAdapter() {    // 窗口关闭事件

            @Override
            public void windowClosing(WindowEvent e){
                try {
                    if (!isOtherClosed.get() && userId != null && enemyId != null) NetClient.getNetClient().sendRemoteDisConnect(userId, enemyId);    // 通知对方下线
                    System.exit(0);
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        // 添加按钮事件
        buttonStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ThreadUtil.threadPoolExecutor.execute(() -> {
                    NetClient netClient = NetClient.getNetClient();
                    netClient.sendChooseCommand(userId);
                });
                choosingDialog.showDialog();
            }
        });

        buttonGiveIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TimerUtil.timer.stop();
                NetClient netClient = NetClient.getNetClient();
                netClient.sendGiveIn(userId, enemyId);
                WinLostDialog winLostDialog = new WinLostDialog(GameClient.getInstance(), "你输了！");
                winLostDialog.showDialog();
            }
        });

        // 悔棋
        buttonAskRegret.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chessBoard.getSteps().isEmpty()) {
                    Opane.notice(GameClient.getInstance(), "不能悔棋");
                    return;
                }

                if (chessBoard.getSteps().size() == 1) {
                    int index = chessBoard.getSteps().get(0).getIndex();
                    short player = (index > 15 ? PlayerType.RED_PLAYER : PlayerType.BLACK_PLAYER);
                    if (player != chessBoard.getOwnerPlayer()) {
                        Opane.notice(GameClient.getInstance(), "不能悔棋");
                        return;
                    }
                }

                NetClient netClient = NetClient.getNetClient();
                netClient.sendRegret(enemyId);
                waitingDialog = new WaitingDialog(null);
                waitingDialog.showDialog();
            }
        });
    }

    public static void main(String[] args) {
        new GameClient();
    }
}
