package org.csu.chinese_chess_server.service;

import org.csu.chinese_chess_server.pojo.User;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/12 下午10:45
 */
public interface UserService {

    User getUserByUserNameAndPassword(String userName, String password);

    User getUser(int id);

    int insertUser(User user);
}
