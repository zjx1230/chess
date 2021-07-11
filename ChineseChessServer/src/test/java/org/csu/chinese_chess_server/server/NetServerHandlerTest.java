package org.csu.chinese_chess_server.server;

import lombok.SneakyThrows;
import org.csu.chinese_chess_server.constant.CommandType;
import org.csu.chinese_chess_server.constant.UserHelper;
import org.csu.chinese_chess_server.dto.Command;
import org.csu.chinese_chess_server.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.csu.chinese_chess_server.server.SessionHolder.isStart;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/7/11 下午6:03
 */
class NetServerHandlerTest {

    @Test
    void getUserByUserNameAndPassword() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        MatchObject matchObject = new MatchObject();

        for (int i = 0; i < 100; i ++) {
            final int userId = i;
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    Random random = new Random();
                    Thread.sleep(random.nextInt(10000));
                    Command command = new Command();
                    command.setUserId(userId);
                    matchObject.matchEnemy(command);
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
    }

    class MatchObject {
        private void matchEnemy(Command command) throws InterruptedException {
            Integer enemyId;
            synchronized (SessionHolder.class) {
                if (isStart.contains(command.getUserId())) return;
                enemyId = SessionHolder.q.poll();
                if (enemyId == null || command.getUserId().equals(enemyId)) {
                    SessionHolder.q.offer(command.getUserId());
                } else {
                    SessionHolder.q.remove(command.getUserId());
                    isStart.add(command.getUserId());
                    isStart.add(enemyId);
                }
            }

            if (enemyId == null || enemyId.equals(command.getUserId())) {
                Command command1 = new Command();
                command1.setCommandType(CommandType.COMMAND_CHOOSE_FAIL);
                Thread.sleep(3000);
                synchronized (SessionHolder.class) {
                    if (isStart.contains(command.getUserId())) return;
                    System.out.println("COMMAND_CHOOSE_FAIL");
                }

                return;
            }

            Command command1 = new Command();
            Command command2 = new Command();
            command1.setEnemyId(enemyId);
            command1.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);
            command2.setEnemyId(command.getUserId());
            command2.setCommandType(CommandType.COMMAND_CHOOSE_SUCCESS);

            Random random = new Random();
            int player = random.nextInt(2);
            command1.setPlayer((short) player);
            command2.setPlayer((short) (1 - player));

            System.out.println(command1);
            System.out.println(command2);
        }
    }
}