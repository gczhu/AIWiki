package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.Message;
import com.zju.se.sem.entity.User;
import com.zju.se.sem.mapper.UserMapper;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @ApiOperation("用户登录认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "email", value = "邮箱"),
    })
    @PostMapping("/login")
    public Message loginIdentify(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getPassword, password);

        List<User> users = userMapper.selectList(queryWrapper);
        if (users.isEmpty()) {
            return new Message(false, "密码错误", 20001);
        } else {
            String token = JwtUtils.generateToken(Integer.toString(users.get(0).getUid()));
            return new Message(true, "认证成功", 20000).data("token", token);
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
    public Message userRegister(String username, String password, String email, int role) {
        // 判断用户名是否已经存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return new Message(false, "用户名已存在", 20001);
        }

        // 判断邮箱地址是否已经存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return new Message(false, "邮箱地址已存在", 20001);
        }

        // 插入用户信息
        User user = new User(username, email, password, role, 0, 1);
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

    @PostMapping("/modifyInfo")
    public Message modifyInfo(@RequestHeader("Authorization") String token,
                              @RequestParam(defaultValue = "0") int uid,
                              @RequestParam(defaultValue = "") String username,
                              @RequestParam(defaultValue = "") String email,
                              @RequestParam(defaultValue = "") String password,
                              @RequestParam(defaultValue = "") String role,
                              @RequestParam(defaultValue = "") String points,
                              @RequestParam(defaultValue = "") String level,
                              @RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "") String gender,
                              @RequestParam(defaultValue = "") String phone,
                              @RequestParam(defaultValue = "") String birth,
                              @RequestParam(defaultValue = "") String image,
                              @RequestParam(defaultValue = "") String description) {

        int UID;
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            UID = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return new Message(false, "用户信息修改失败，token错误", 50001);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUid, UID);
        User user = userMapper.selectList(queryWrapper).get(0);

        if (username.length() != 0) {
            // 判断用户名是否已经存在
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUsername, username);
            List<User> users = userMapper.selectList(queryWrapper1);
            if (!users.isEmpty()) {
                return new Message(false, "用户名重复", 20001);
            }
            user.setUsername(username);
        }
        if (email.length() != 0) {
            // 判断邮箱地址是否已经存在
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getEmail, email);
            List<User> users = userMapper.selectList(queryWrapper1);
            if (!users.isEmpty()) {
                return new Message(false, "邮箱地址重复", 20001);
            }
            user.setEmail(email);
        }
        if (password.length() != 0) {
            user.setPassword(password);
        }
        if (role.length() != 0) {
            user.setRole(Integer.parseInt(role));
        }
        if (points.length() != 0) {
            user.setPoints(Integer.parseInt(points));
        }
        if (level.length() != 0) {
            user.setLevel(Integer.parseInt(level));
        }
        if (name.length() != 0) {
            user.setName(name);
        }
        if (gender.length() != 0) {
            user.setGender(gender.toCharArray()[0]);
        }
        if (phone.length() != 0) {
            user.setPhone(phone);
        }
        if (birth.length() != 0) {
            user.setBirth(birth);
        }
        if (image.length() != 0) {
            user.setImage(image);
        }
        if (description.length() != 0) {
            user.setDescription(description);
        }

        try {
            int res = userMapper.update(user, queryWrapper);
            if (res > 0) {
                return new Message(true, "用户信息修改成功", 20000);
            } else {
                return new Message(false, "用户信息修改失败", 20001);
            }
        } catch (Exception e) {
            return new Message(false, "用户信息修改失败", 20001);
        }
    }
}
