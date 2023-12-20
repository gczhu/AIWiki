package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.*;
import com.zju.se.sem.mapper.EntryMapper;
import com.zju.se.sem.mapper.UserEntryMapper;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */
@RestController
@CrossOrigin
public class EntryController {
    @Autowired
    private EntryMapper entryMapper;
    @Autowired
    private UserEntryMapper userEntryMapper;

    @GetMapping("/topics")
    public Message getTopics(@RequestHeader("Authorization") String token) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            LambdaQueryWrapper<UserEntry> userEntryQueryWrapper = new LambdaQueryWrapper<>();
            userEntryQueryWrapper.eq(UserEntry::getUid, uid)
                    .apply("CONVERT(eid, CHAR) LIKE '1%'");
            List<UserEntry> userEntries = userEntryMapper.selectList(userEntryQueryWrapper);

            List<Topic> topics = new ArrayList<>();
            for (int i = 0; i < userEntries.size(); i++) {
                LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                entryQueryWrapper.eq(Entry::getEid, userEntries.get(i).getEid());
                Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                topics.add(new Topic(entry.getEid(), entry.getTitle(), entry.getDescription(), entry.getCategory(), entry.getImage()));
            }
            return new Message(true, "专题信息获取成功", 20000)
                    .data("topics", topics);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(false, "专题信息获取失败", 20001);
        }
    }

    @GetMapping("/entry")
    public Message getEntries(@RequestHeader("Authorization") String token) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            LambdaQueryWrapper<UserEntry> userEntryQueryWrapper = new LambdaQueryWrapper<>();
            userEntryQueryWrapper.eq(UserEntry::getUid, uid)
                    .apply("CONVERT(eid, CHAR) LIKE '3%'");
            List<UserEntry> userEntries = userEntryMapper.selectList(userEntryQueryWrapper);

            List<EntryIndex> entryIndices = new ArrayList<>();
            for (int i = 0; i < userEntries.size(); i++) {
                LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                entryQueryWrapper.eq(Entry::getEid, userEntries.get(i).getEid());
                Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                entryIndices.add(new EntryIndex(entry.getEid(), entry.getTitle(), entry.getCategory()));
            }
            return new Message(true, "词条索引获取成功", 20000)
                    .data("entries", entryIndices);
        } catch (Exception e) {
            return new Message(false, "词条索引获取失败", 20001);
        }
    }

    @GetMapping("/goodEntry")
    public Message getGoodEntries() {
        try {
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.apply("CONVERT(eid, CHAR) LIKE '1%'");
            List<Entry> entries = entryMapper.selectList(entryQueryWrapper);

            // 使用 Comparator 对象按照 time 属性进行排序
            Collections.sort(entries, new Comparator<Entry>() {
                @Override
                public int compare(Entry entry1, Entry entry2) {
                    Double points1 = entry1.getVisits() * 0.05 + entry1.getLikes() * 0.35 + entry1.getFavors() * 0.6;
                    Double points2 = entry2.getVisits() * 0.05 + entry2.getLikes() * 0.35 + entry2.getFavors() * 0.6;
                    return points2.compareTo(points1);
                }
            });

            // 计算前20%的词条数量
            int top20Percent = Math.max((int) (entries.size() * 0.2), 1);
            // 获取前20%的词条
            List<GoodEntry> goodEntries = new ArrayList<>();
            for (int i = 0; i < top20Percent; i++) {
                Entry entry = entries.get(i);
                goodEntries.add(new GoodEntry(entry.getEid(), entry.getTitle()));
            }

            return new Message(true, "优质词条获取成功", 20000)
                    .data("goodEntries", goodEntries);
        } catch (Exception e) {
            return new Message(false, "优质词条获取失败", 20001);
        }
    }

    @GetMapping("/entryInfo")
    public Message getEntryInfo(@RequestHeader("Authorization") String token, @RequestParam int id) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            LambdaQueryWrapper<UserEntry> userEntryQueryWrapper = new LambdaQueryWrapper<>();
            userEntryQueryWrapper.eq(UserEntry::getUid, uid)
                    .ne(UserEntry::getEid, id)
                    .apply("CONVERT(eid, CHAR) LIKE '1%'");
            List<UserEntry> userEntries = userEntryMapper.selectList(userEntryQueryWrapper);

            List<GoodEntry> goodEntries = new ArrayList<>();
            for (int i = 0; i < userEntries.size(); i++) {
                LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                entryQueryWrapper.eq(Entry::getEid, userEntries.get(i).getEid());
                Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                goodEntries.add(new GoodEntry(entry.getEid(), entry.getTitle()));
            }

            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.eq(Entry::getEid, id);
            Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
            EntryInfo entryInfo = new EntryInfo(id, entry.getTitle(), entry.getContent(), goodEntries);
            return new Message(true, "词条详情获取成功", 20000)
                    .data("entryInfo", entryInfo);
        } catch (Exception e) {
            return new Message(false, "词条详情获取失败", 20001);
        }
    }

    @PostMapping("/modifyEntry")
    public Message modifyEntry(@RequestHeader("Authorization") String token,
                               @RequestParam(defaultValue = "0") int id,
                               @RequestParam(defaultValue = "") String title,
                               @RequestParam(defaultValue = "") String text) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if (id == 0) {
                return new Message(false, "修改词条失败", 20001);
            }

            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.eq(Entry::getEid, id);
            Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);

            if (title.length() != 0) {
                entry.setTitle(title);
            }
            if (text.length() != 0) {
                entry.setContent(text);
            }

            int res = entryMapper.update(entry, entryQueryWrapper);
            if (res > 0) {
                return new Message(true, "修改内容已上报，请等待管理员审核", 20000);
            } else {
                return new Message(false, "修改词条失败", 20001);
            }

        } catch (Exception e) {
            return new Message(false, "修改词条失败", 20001);
        }
    }

    @PostMapping("/addEntry")
    public Message addEntry(@RequestHeader("Authorization") String token,
                            @RequestParam(defaultValue = "0") int id,
                            @RequestParam(defaultValue = "") String title,
                            @RequestParam(defaultValue = "") String text,
                            @RequestParam(defaultValue = "") String description,
                            @RequestParam(defaultValue = "") String category) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }

            Entry entry = new Entry();

            if (id != 0) {
                entry.setEid(id);
            } else {
                LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                entryQueryWrapper.apply("CONVERT(eid, CHAR) LIKE '1%'")
                        .orderByDesc(Entry::getEid)
                        .last("LIMIT 1");
                Entry entry1 = entryMapper.selectOne(entryQueryWrapper);
                if (entry1 != null) {
                    int eid = entry1.getEid();
                    String s1 = Integer.toString(eid + 1);
                    if (s1.charAt(0) == '2') {
                        s1 = "1";
                        for (int i = 0; i < s1.length(); i++) {
                            s1 = s1 + "0";
                        }
                        entry.setEid(Integer.parseInt(s1));
                    } else {
                        entry.setEid(entry1.getEid() + 1);
                    }
                } else {
                    entry.setEid(1000);
                }
            }
            if (title.length() != 0) {
                entry.setTitle(title);
            }
            if (text.length() != 0) {
                entry.setContent(text);
            }
            if (description.length() != 0) {
                entry.setDescription(description);
            }
            if (category.length() != 0) {
                entry.setCategory(category);
            }

            int res = entryMapper.insert(entry);
            if (res > 0) {
                return new Message(true, "词条内容已上报，请等待管理员审核", 20000);
            } else {
                return new Message(false, "添加词条失败", 20001);
            }

        } catch (Exception e) {
            return new Message(false, "添加词条失败", 20001);
        }
    }

}
