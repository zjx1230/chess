package org.csu.chinese_chess_server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.chinese_chess_server.pojo.User;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/4/2 11:28 上午
 */
@Mapper
public interface UserDao {

    User getUserByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    User getUser(int id);

    int insertUser(User user);
}
