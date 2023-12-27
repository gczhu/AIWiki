package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.*;
import com.zju.se.sem.mapper.*;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 祝广程
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = "*")
public class EntryController {
    @Autowired
    private EntryMapper entryMapper;
    @Autowired
    private UserEntryMapper userEntryMapper;
    @Autowired
    private EntryTagMapper entryTagMapper;
    @Autowired
    private ErrorCorrectionMapper errorCorrectionMapper;
    @Autowired
    private EntrySubmissionMapper entrySubmissionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/topics")
    public Message getTopics(@RequestParam(defaultValue = "0") int uid) {
        try {
            // 如果没有指定uid，则返回所有topics
            if (uid == 0) {
                LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                entryQueryWrapper.apply("CONVERT(eid, CHAR) LIKE '1%'");
                List<Entry> entries = entryMapper.selectList(entryQueryWrapper);

                List<Topic> topics = new ArrayList<>();
                for (int i = 0; i < entries.size(); i++) {
                    Entry entry = entries.get(i);
                    topics.add(new Topic(entry.getEid(), entry.getTitle(), entry.getDescription(), entry.getCategory(),
                            entry.getImage()));
                }

                return new Message(true, "专题信息获取成功", 20000)
                        .data("topics", topics);
            } else { // 如果指定了uid，则进行个性化推荐
                // 查找与该用户相关联的词条ID
                LambdaQueryWrapper<UserEntry> userEntryQueryWrapper = new LambdaQueryWrapper<>();
                userEntryQueryWrapper.eq(UserEntry::getUid, uid)
                        .eq(UserEntry::getType, "recommend")
                        .apply("CONVERT(eid, CHAR) LIKE '1%'");
                List<UserEntry> userEntries = userEntryMapper.selectList(userEntryQueryWrapper);

                // 如果没有该用户的指定推荐词条（新用户），则返回所有topics
                if (userEntries.size() == 0) {
                    LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                    entryQueryWrapper.apply("CONVERT(eid, CHAR) LIKE '1%'");
                    List<Entry> entries = entryMapper.selectList(entryQueryWrapper);

                    List<Topic> topics = new ArrayList<>();
                    for (int i = 0; i < entries.size(); i++) {
                        Entry entry = entries.get(i);
                        topics.add(new Topic(entry.getEid(), entry.getTitle(), entry.getDescription(),
                                entry.getCategory(), entry.getImage()));
                    }

                    return new Message(true, "专题信息获取成功", 20000)
                            .data("topics", topics);
                }

                // 如果有该用户的指定推荐词条，则根据词条ID查询各词条信息并以列表返回
                List<Topic> topics = new ArrayList<>();
                for (int i = 0; i < userEntries.size(); i++) {
                    LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                    entryQueryWrapper.eq(Entry::getEid, userEntries.get(i).getEid());
                    Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                    topics.add(new Topic(entry.getEid(), entry.getTitle(), entry.getDescription(), entry.getCategory(),
                            entry.getImage()));
                }

                return new Message(true, "专题信息获取成功", 20000)
                        .data("topics", topics);
            }
        } catch (Exception e) {
            return new Message(false, "专题信息获取失败", 20001);
        }
    }

    @GetMapping("/entry")
    public Message getEntries() {
        try {
            // 查找所有小词条索引
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.apply("CONVERT(eid, CHAR) LIKE '3%'");
            List<Entry> entries = entryMapper.selectList(entryQueryWrapper);

            // 根据小词条索引ID查询各词条信息并返回
            List<EntryIndex> entryIndices = new ArrayList<>();
            for (int i = 0; i < entries.size(); i++) {
                Entry entry = entries.get(i);
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
            // 获取所有的词条
            List<Entry> entries = entryMapper.selectList(new LambdaQueryWrapper<>());

            // 使用 Comparator 对象按照评分（访问量、点赞数、收藏数加权平均）进行排序
            Collections.sort(entries, new Comparator<Entry>() {
                @Override
                public int compare(Entry entry1, Entry entry2) {
                    Double points1 = entry1.getVisits() * 0.05 + entry1.getLikes() * 0.35 + entry1.getFavors() * 0.6;
                    Double points2 = entry2.getVisits() * 0.05 + entry2.getLikes() * 0.35 + entry2.getFavors() * 0.6;
                    return points2.compareTo(points1);
                }
            });

            // 计算前30%的词条数量
            int goodEntriesNum = 0;
            if (entries.size() != 0) {
                goodEntriesNum = Math.max((int) (entries.size() * 0.3), 1);
            }
            // 获取前30%的词条
            List<GoodEntry> goodEntries = new ArrayList<>();
            for (int i = 0; i < goodEntriesNum; i++) {
                Entry entry = entries.get(i);
                goodEntries.add(new GoodEntry(entry.getEid(), entry.getTitle()));
            }

            return new Message(true, "优质词条获取成功", 20000)
                    .data("goodEntries", goodEntries);
        } catch (Exception e) {
            return new Message(false, "优质词条获取失败", 20001);
        }
    }

    @GetMapping("/relatedEntry")
    public Message relatedEntry(@RequestParam int id) {
        try {
            Set<GoodEntry> relatedEntries = new HashSet<>();
            // 获取该词条拥有的标签
            LambdaQueryWrapper<EntryTag> entryTagQueryWrapper = new LambdaQueryWrapper<>();
            entryTagQueryWrapper.eq(EntryTag::getEid, id);
            List<EntryTag> entryTags = entryTagMapper.selectList(entryTagQueryWrapper);

            // 获取拥有相同标签的词条
            for (int i = 0; i < entryTags.size(); i++) {
                String tag = entryTags.get(i).getTag();
                LambdaQueryWrapper<EntryTag> entryTagQueryWrapper1 = new LambdaQueryWrapper<>();
                entryTagQueryWrapper1.eq(EntryTag::getTag, tag);
                List<EntryTag> entryTags1 = entryTagMapper.selectList(entryTagQueryWrapper1);

                for (int j = 0; j < entryTags1.size(); j++) {
                    int eid = entryTags1.get(j).getEid();
                    if (eid != id) {
                        LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                        entryQueryWrapper.eq(Entry::getEid, eid);
                        Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                        GoodEntry sameTagEntry = new GoodEntry(entry.getEid(), entry.getTitle());
                        relatedEntries.add(sameTagEntry);
                    }
                }
            }

            /* 将优质词条加入relatedEntries（避免由于没有与该词条标签相同的词条，导致返回空列表） */
            // 获取除该词条外的所有词条
            LambdaQueryWrapper<Entry> entryQueryWrapper1 = new LambdaQueryWrapper<>();
            entryQueryWrapper1.ne(Entry::getEid, id);
            List<Entry> allEntries = entryMapper.selectList(entryQueryWrapper1);

            // 使用 Comparator 对象按照评分（访问量、点赞数、收藏数加权平均）进行排序
            Collections.sort(allEntries, new Comparator<Entry>() {
                @Override
                public int compare(Entry entry1, Entry entry2) {
                    Double points1 = entry1.getVisits() * 0.05 + entry1.getLikes() * 0.35 + entry1.getFavors() * 0.6;
                    Double points2 = entry2.getVisits() * 0.05 + entry2.getLikes() * 0.35 + entry2.getFavors() * 0.6;
                    return points2.compareTo(points1);
                }
            });

            // 计算前30%的词条数量
            int goodEntriesNum = 0;
            if (allEntries.size() != 0) {
                goodEntriesNum = Math.max((int) (allEntries.size() * 0.3), 1);
            }
            // 获取前30%的词条并加入relatedEntries
            for (int i = 0; i < goodEntriesNum; i++) {
                Entry entry = allEntries.get(i);
                GoodEntry goodEntry = new GoodEntry(entry.getEid(), entry.getTitle());
                relatedEntries.add(goodEntry);
            }

            return new Message(true, "相关词条获取成功", 20000)
                    .data("relation", new ArrayList<>(relatedEntries));
        } catch (Exception e) {
            return new Message(false, "相关词条获取失败", 20001);
        }
    }

    @GetMapping("/entryInfo")
    public Message getEntryInfo(@RequestParam int id) {
        try {
            /* 获取相关词条 */
            Set<GoodEntry> relatedEntries = new LinkedHashSet<>();
            // 获取该词条拥有的标签
            LambdaQueryWrapper<EntryTag> entryTagQueryWrapper = new LambdaQueryWrapper<>();
            entryTagQueryWrapper.eq(EntryTag::getEid, id);
            List<EntryTag> entryTags = entryTagMapper.selectList(entryTagQueryWrapper);

            // 获取拥有相同标签的词条
            for (int i = 0; i < entryTags.size(); i++) {
                String tag = entryTags.get(i).getTag();
                LambdaQueryWrapper<EntryTag> entryTagQueryWrapper1 = new LambdaQueryWrapper<>();
                entryTagQueryWrapper1.eq(EntryTag::getTag, tag);
                List<EntryTag> entryTags1 = entryTagMapper.selectList(entryTagQueryWrapper1);

                for (int j = 0; j < entryTags1.size(); j++) {
                    int eid = entryTags1.get(j).getEid();
                    if (eid != id) {
                        LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
                        entryQueryWrapper.eq(Entry::getEid, eid);
                        Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
                        GoodEntry sameTagEntry = new GoodEntry(entry.getEid(), entry.getTitle());
                        relatedEntries.add(sameTagEntry);
                    }
                }
            }

            /* 将优质词条加入relatedEntries（避免由于没有与该词条标签相同的词条，导致返回空列表） */
            // 获取除该词条外的所有词条
            LambdaQueryWrapper<Entry> entryQueryWrapper1 = new LambdaQueryWrapper<>();
            entryQueryWrapper1.ne(Entry::getEid, id);
            List<Entry> allEntries = entryMapper.selectList(entryQueryWrapper1);

            // 使用 Comparator 对象按照评分（访问量、点赞数、收藏数加权平均）进行排序
            Collections.sort(allEntries, new Comparator<Entry>() {
                @Override
                public int compare(Entry entry1, Entry entry2) {
                    Double points1 = entry1.getVisits() * 0.05 + entry1.getLikes() * 0.35 + entry1.getFavors() * 0.6;
                    Double points2 = entry2.getVisits() * 0.05 + entry2.getLikes() * 0.35 + entry2.getFavors() * 0.6;
                    return points2.compareTo(points1);
                }
            });

            // 计算前30%的词条数量
            int goodEntriesNum = 0;
            if (allEntries.size() != 0) {
                goodEntriesNum = Math.max((int) (allEntries.size() * 0.3), 1);
            }
            // 获取前30%的词条并加入relatedEntries
            for (int i = 0; i < goodEntriesNum; i++) {
                Entry entry = allEntries.get(i);
                GoodEntry goodEntry = new GoodEntry(entry.getEid(), entry.getTitle());
                relatedEntries.add(goodEntry);
            }

            /* 返回词条信息和相关词条 */
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.eq(Entry::getEid, id);
            Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
            // 将该词条的访问数加一并写回数据库
            entry.setVisits(entry.getVisits() + 1);
            int res = entryMapper.update(entry, entryQueryWrapper);
            if (res <= 0) {
                return new Message(false, "词条详情获取失败", 20001);
            }
            EntryInfo entryInfo = new EntryInfo(id, entry.getTitle(), entry.getContent(),
                    new ArrayList<>(relatedEntries));

            return new Message(true, "词条详情获取成功", 20000)
                    .data("entryInfo", entryInfo);
        } catch (Exception e) {
            return new Message(false, "词条详情获取失败", 20001);
        }
    }

    @GetMapping("/search")
    public Message search(@RequestParam String keyword) {
        try {
            Set<Entry> resultEntries = new LinkedHashSet<>();

            // 获取拥有与关键字相同的标签的词条
            LambdaQueryWrapper<EntryTag> entryTagQueryWrapper = new LambdaQueryWrapper<>();
            entryTagQueryWrapper.eq(EntryTag::getTag, keyword);
            List<EntryTag> entryTags = entryTagMapper.selectList(entryTagQueryWrapper);

            for (int i = 0; i < entryTags.size(); i++) {
                int eid = entryTags.get(i).getEid();
                LambdaQueryWrapper<Entry> entryQueryWrapper1 = new LambdaQueryWrapper<>();
                entryQueryWrapper1.eq(Entry::getEid, eid);
                Entry entry = entryMapper.selectList(entryQueryWrapper1).get(0);
                resultEntries.add(entry);
            }

            // 获取文本内容包含关键字的词条
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.apply("content LIKE {0}", "%" + keyword + "%");
            List<Entry> entries = entryMapper.selectList(entryQueryWrapper);
            resultEntries.addAll(entries);

            List<Entry> res = new ArrayList<>(resultEntries);
            if (res.size() == 0) { // 如果没有根据标签和文本内容搜索到相关内容，就返回优质词条
                // 获取所有的词条
                List<Entry> allEntries = entryMapper.selectList(new LambdaQueryWrapper<>());

                // 使用 Comparator 对象按照评分（访问量、点赞数、收藏数加权平均）进行排序
                Collections.sort(allEntries, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry entry1, Entry entry2) {
                        Double points1 = entry1.getVisits() * 0.05 + entry1.getLikes() * 0.35
                                + entry1.getFavors() * 0.6;
                        Double points2 = entry2.getVisits() * 0.05 + entry2.getLikes() * 0.35
                                + entry2.getFavors() * 0.6;
                        return points2.compareTo(points1);
                    }
                });

                // 计算前30%的词条数量
                int goodEntriesNum = 0;
                if (allEntries.size() != 0) {
                    goodEntriesNum = Math.max((int) (allEntries.size() * 0.3), 1);
                }
                // 获取前30%的词条
                for (int i = 0; i < goodEntriesNum; i++) {
                    Entry entry = allEntries.get(i);
                    res.add(entry);
                }
                return new Message(true, "未搜索到相关词条信息，为您推荐以下内容：", 20000)
                        .data("searchRes", res);
            } else {
                return new Message(true, "搜索成功，以下是相关词条信息：", 20000)
                        .data("searchRes", res);
            }
        } catch (Exception e) {
            return new Message(false, "搜索失败", 20001);
        }
    }

    @PostMapping("/modifyEntry")
    public Message modifyEntry(@RequestHeader("Authorization") String token,
            @RequestBody Entry entry) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if (entry.getEid() == 0) {
                return new Message(false, "修改词条失败", 20001);
            }
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.eq(Entry::getEid, entry.getEid());

            int role = userMapper.selectById(uid).getRole();
            if(role == 1){
                if(entryMapper.update(entry, entryQueryWrapper) > 0){
                    return new Message(true, "词条已修改", 20000);
                }
                else{
                    return new Message(false, "修改词条失败", 20001);
                }
            }
            else{
                ErrorCorrection errorCorrection = new ErrorCorrection(uid, entry);
                int res = errorCorrectionMapper.insert(errorCorrection);
                if (res > 0) {
                    return new Message(true, "修改内容已上报，请等待管理员审核", 20000);
                } else {
                    return new Message(false, "修改词条失败", 20001);
                }
            }
        } catch (Exception e) {
            return new Message(false, "修改词条失败", 20001);
        }
    }

    @PostMapping("/addEntry")
    public Message addEntry(@RequestHeader("Authorization") String token,
            @RequestBody Entry entry) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }

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

            int role = userMapper.selectById(uid).getRole();
            if(role == 1){
                if(entryMapper.insert(entry) > 0){
                    return new Message(true, "词条已添加", 20000);
                }
                else{
                    return new Message(false, "添加词条失败", 20001);
                }
            }
            else{
                EntrySubmission entrySubmission = new EntrySubmission(uid, entry);
                int res = entrySubmissionMapper.insert(entrySubmission);
                if (res > 0) {
                    return new Message(true, "词条内容已上报，请等待管理员审核", 20000);
                } else {
                    return new Message(false, "添加词条失败", 20001);
                }
            }
        } catch (Exception e) {
            return new Message(false, "添加词条失败", 20001);
        }
    }

    @PostMapping("/likeEntry")
    public Message likeEntry(
            @RequestParam(defaultValue = "0") int id,
            @RequestParam(defaultValue = "0") int uid) {
        try {
            if (id == 0) {
                return new Message(false, "点赞词条失败", 20001);
            }
            LambdaQueryWrapper<Entry> entryQueryWrapper = new LambdaQueryWrapper<>();
            entryQueryWrapper.eq(Entry::getEid, id);
            Entry entry = entryMapper.selectList(entryQueryWrapper).get(0);
            UserEntry user_entry = new UserEntry();
            if (uid != 0) {
                user_entry.setEid(id);
                user_entry.setUid(uid);
                user_entry.setType("like");
                userEntryMapper.insert(user_entry);
            }
            entry.setLikes(entry.getLikes() + 1);
            int res = entryMapper.update(entry, entryQueryWrapper);
            if (res > 0) {
                return new Message(true, "点赞词条成功", 20000);
            } else {
                return new Message(false, "点赞词条失败", 20001);
            }

        } catch (Exception e) {
            return new Message(false, "点赞词条失败", 20001);
        }
    }

    @PostMapping("/favorEntry")
    public Message favorEntry(
            @RequestParam(defaultValue = "0") int id,
            @RequestParam(defaultValue = "0") int uid) {
        try {
            if (id == 0 || uid == 0) {
                return new Message(false, "收藏词条失败", 20001);
            }
            UserEntry user_entry = new UserEntry();
            user_entry.setEid(id);
            user_entry.setUid(uid);
            user_entry.setType("favor");
            int res = userEntryMapper.insert(user_entry);

            if (res > 0) {
                return new Message(true, "收藏词条成功", 20000);
            } else {
                return new Message(false, "收藏词条失败", 20001);
            }

        } catch (Exception e) {
            return new Message(false, "收藏词条失败", 20001);
        }
    }

    @PostMapping("/recommendEntry")
    public Message recommendEntry(
            @RequestParam(defaultValue = "0") int uid) {
        try {
            if (uid == 0) {
                return new Message(false, "推荐词条查询失败", 20001);
            }
            LambdaQueryWrapper<UserEntry> UserEntryQueryWrapper = new LambdaQueryWrapper<>();
            UserEntryQueryWrapper.eq(UserEntry::getUid, uid).eq(UserEntry::getType, "recommend");
            List<UserEntry> recommend_eids = userEntryMapper.selectList(UserEntryQueryWrapper);
            List<Entry> recommend = new ArrayList<>();
            for (int i = 0; i < recommend_eids.size(); i++) {
                LambdaQueryWrapper<Entry> EntryQueryWrapper = new LambdaQueryWrapper<>();
                EntryQueryWrapper.eq(Entry::getEid, recommend_eids.get(i).getEid());
                recommend.add(entryMapper.selectOne(EntryQueryWrapper));
            }

            return new Message(true, "收藏词条成功", 20000)
                    .data("recommend", recommend);

        } catch (Exception e) {
            return new Message(false, "收藏词条失败", 20001)
                    .data("recommend", new ArrayList<Entry>());
        }
    }

}
