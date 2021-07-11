package org.csu.chinese_chess_server.service.impl;

import org.csu.chinese_chess_server.mapper.UserDao;
import org.csu.chinese_chess_server.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 *
 * @author zjx
 * @since 2021/6/14 下午6:51
 */
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserDao userDao;

    @Test
    void getUserByUserNameAndPassword() {
        String userName = "a", password = "a";
        User user = userDao.getUserByUserNameAndPassword(userName, password);
        if (user == null) {
            System.out.println("aa");
        } else {
            System.out.println(user);
        }
    }
}