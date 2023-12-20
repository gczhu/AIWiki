package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.Message;
import com.zju.se.sem.entity.Tool;
import com.zju.se.sem.entity.User;
import com.zju.se.sem.mapper.ToolMapper;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */
@RestController
@CrossOrigin
public class ToolController {
    @Autowired
    private ToolMapper toolMapper;

    @GetMapping("/getTool")
    public Message getTool(@RequestHeader("Authorization") String token, String id) {
        // 校验token
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return new Message(false, "工具信息获取失败，token错误", 50001);
        }

        // 根据id查找工具信息
        LambdaQueryWrapper<Tool> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tool::getTid, id);

        List<Tool> tools = toolMapper.selectList(queryWrapper);
        if (tools.isEmpty()) {
            return new Message(false, "该工具不存在", 20001);
        } else {
            return new Message(true, "工具信息获取成功", 20000).data("tool", tools.get(0));
        }
    }

    @GetMapping("/recommendTool")
    public Message recommendTool(@RequestHeader("Authorization") String token) {
        // 校验token
        try {
            Claims claims = JwtUtils.getClaimsByToken(token);
            Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return new Message(false, "推荐工具获取失败，token错误", 50001);
        }

        // 获取所有工具信息
        LambdaQueryWrapper<Tool> queryWrapper = new LambdaQueryWrapper<>();
        List<Tool> tools = toolMapper.selectList(queryWrapper);
        return new Message(true, "推荐工具获取成功", 20000).data("tools", tools.get(0));
    }

}
