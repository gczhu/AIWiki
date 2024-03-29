package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.Message;
import com.zju.se.sem.entity.User;
import com.zju.se.sem.mapper.UserMapper;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public Message login(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        queryWrapper.eq(User::getPassword, user.getPassword());

        List<User> users = userMapper.selectList(queryWrapper);
        if (users.isEmpty()) {
            return new Message(false, "密码错误", 20001);
        } else {
            int uid = users.get(0).getUid();
            String token = JwtUtils.generateToken(Integer.toString(uid));
            return new Message(true, "认证成功", 20000)
                    .data("token", token)
                    .data("uid", uid);
        }
    }

    @PostMapping("/logout")
    public Message logout(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            int uid = Integer.parseInt(claims.getSubject());
            return new Message(true, "用户" + uid + "登出成功", 20000);
        } catch (Exception e) {
            return new Message(false, "登出失败，token错误", 50001);
        }
    }

    @PostMapping("/register")
    public Message userRegister(@RequestBody User user) {
        // 判断用户名是否已经存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return new Message(false, "用户名已存在", 20001);
        }

        // 判断邮箱地址是否已经存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return new Message(false, "邮箱地址已存在", 20001);
        }

        if (userMapper.insert(user) > 0) {
            return new Message(true, "注册成功", 20000);
        } else {
            return new Message(false, "注册失败", 20001);
        }
    }

    @GetMapping("/getInfo")
    public Message userInfo(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            int uid = Integer.parseInt(claims.getSubject());
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUid, uid);

            User user = userMapper.selectList(queryWrapper).get(0);
            return new Message(true, "用户信息获取成功", 20000)
                    .data("uid", user.getUid())
                    .data("username", user.getUsername())
                    .data("email", user.getEmail())
                    .data("password", user.getPassword())
                    .data("role", user.getRole())
                    .data("points", user.getPoints())
                    .data("level", user.getLevel())
                    .data("name", user.getUsername())
                    .data("gender", user.getGender())
                    .data("phone", user.getPhone())
                    .data("birth", user.getBirth())
                    .data("img_url", user.getImage())
                    .data("description", user.getDescription());
        } catch (Exception e) {
            return new Message(false, "用户信息获取失败，token错误", 50001);
        }
    }
/**
 * {
    "role": 0,
    "gender": "",
    "level": 0,
    "birth": null,
    "description": "",
    "points": 0,
    "uid": 2,
    "password": "123456",
    "phone": "15377273704",
    "img_url": "",
    "name": "zhangsan",
    "email": "1120712503@qq.com",
    "username": "zhangsan",
    "address": "浙江省杭州市"
}
 */
    @PostMapping("/modifyInfo")
    public Message modifyInfo(@RequestHeader("Authorization") String token, @RequestBody User user) {

        int uid = 0;
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            uid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return new Message(false, "用户信息修改失败，token错误", 50001);
        }

//        if (uid == 0) {
//            return new Message(true, "用户信息修改成功", 20000);
//        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUid, uid);

        if (user.getUsername().length() != 0) {
            // 判断用户名是否已经存在
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUsername, user.getUsername());
            List<User> users = userMapper.selectList(queryWrapper1);
            if (!users.isEmpty() && users.get(0).getUid() != uid) {
                return new Message(false, "用户名重复", 20001);
            }
        }
        if (user.getEmail().length() != 0 ) {
            // 判断邮箱地址是否已经存在
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getEmail, user.getEmail());
            List<User> users = userMapper.selectList(queryWrapper1);
            if (!users.isEmpty() && users.get(0).getUid() != uid) {
                return new Message(false, "邮箱地址重复", 20001);
            }
        }

        try {
            int res = userMapper.update(user, queryWrapper);
            if (res > 0) {
                return new Message(true, "用户信息修改成功", 20000);
            } else {
                return new Message(true, "用户信息修改成功", 20000);
            }
        } catch (Exception e) {
            return new Message(false, "用户信息修改失败", 20001);
        }
    }
}
