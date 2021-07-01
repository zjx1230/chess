package org.csu.chinese_chess_server.service.impl;

import org.csu.chinese_chess_server.mapper.UserDao;
import org.csu.chinese_chess_server.pojo.User;
import org.csu.chinese_chess_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/12 下午10:46
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUserNameAndPassword(String userName, String password) {
        return userDao.getUserByUserNameAndPassword(userName, password);
    }

    @Override
    public User getUser(int id) {
        return userDao.getUser(id);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }
}
