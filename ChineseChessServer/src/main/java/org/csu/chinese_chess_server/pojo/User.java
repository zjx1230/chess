package org.csu.chinese_chess_server.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/11 11:55 下午
 */
@Alias("user")
@Data
public class User {

    private int id;

    private String userName;

    private String password;

}
