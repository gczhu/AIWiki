package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("entry_submission")
public class EntrySubmission {
    @TableId(type = IdType.AUTO)
    private int esid;
    private int uid;
    private int eid;
    private String title;
    private String content;
    private String description;
    private String category;
    private String image;
    private LocalDateTime submitTime;
    private String status;
    private int admin;

    public EntrySubmission(int uid, int eid, String title, String content, LocalDateTime submitTime, String status, int admin) {
        this.uid = uid;
        this.eid = eid;
        this.title = title;
        this.content = content;
        this.submitTime = submitTime;
        this.status = status;
        this.admin = admin;
    }
    public EntrySubmission(int uid, Entry entry){
        this.uid = uid;
        this.eid = entry.getEid();
        this.title = entry.getTitle();
        this.content = entry.getContent();
        this.description = entry.getDescription();
        this.category = entry.getCategory();
        this.image = entry.getImage();
        this.submitTime = LocalDateTime.now();
        this.status = "待处理";
        this.admin = 1; // 默认交给admin管理员处理
    }

    public EntrySubmission() {
    }
}
