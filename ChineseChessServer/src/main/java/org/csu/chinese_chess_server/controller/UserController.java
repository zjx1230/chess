package org.csu.chinese_chess_server.controller;

import org.csu.chinese_chess_server.constant.UserHelper;
import org.csu.chinese_chess_server.pojo.User;
import org.csu.chinese_chess_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author zjx
 * @since 2021/6/11 8:31 下午
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        User user1 = userService.getUserByUserNameAndPassword(user.getUserName(), user.getPassword());

        if (user1 == null) return "-1";
        return String.valueOf(user1.getId());
    }

    @PostMapping("/register")
    public Object register(@RequestBody User user) {
        try {
            int id = userService.insertUser(user);
            UserHelper.userMap.put(id, user.getUserName());
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

}
