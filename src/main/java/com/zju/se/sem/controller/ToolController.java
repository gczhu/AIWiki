package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.*;
import com.zju.se.sem.mapper.ToolMapper;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = "*")
public class ToolController {
    @Autowired
    private ToolMapper toolMapper;

    @GetMapping("/getTool")
    public Message getTool() {
        LambdaQueryWrapper<Tool> toolQueryWrapper = new LambdaQueryWrapper<>();
        List<Tool> tools = toolMapper.selectList(toolQueryWrapper);
        List<ToolInfo> res = new ArrayList<>();
        for (int i = 0; i < tools.size(); i++) {
            Tool tool = tools.get(i);
            res.add(new ToolInfo(tool.getTid(), tool.getTitle(), tool.getHref(), tool.getUrl(), tool.getDescription(), tool.getCategory()));
        }
        return new Message(true, "推荐工具获取成功", 20000).data("tools", res);
    }

    @GetMapping("/recommendTool")
    public Message recommendTool() {
        LambdaQueryWrapper<Tool> toolQueryWrapper = new LambdaQueryWrapper<>();
        toolQueryWrapper.eq(Tool::getRecommend, 1);
        List<Tool> tools = toolMapper.selectList(toolQueryWrapper);
        List<ToolInfo> res = new ArrayList<>();
        for (int i = 0; i < tools.size(); i++) {
            Tool tool = tools.get(i);
            res.add(new ToolInfo(tool.getTid(), tool.getTitle(), tool.getHref(), tool.getUrl(), tool.getDescription(), tool.getCategory()));
        }
        return new Message(true, "推荐工具获取成功", 20000).data("tools", res);
    }

}
