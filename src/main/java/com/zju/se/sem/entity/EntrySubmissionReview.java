package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("entry_submission_review")
public class EntrySubmissionReview {
    @TableId(type = IdType.AUTO)
    private int esrid;
    private int admin;
    private int esid;
    private String status;
    private String comment;
    private LocalDateTime reviewTime;

    public EntrySubmissionReview(int esrid, int admin, int esid, String status, String comment, LocalDateTime reviewTime) {
        this.esrid = esrid;
        this.admin = admin;
        this.esid = esid;
        this.status = status;
        this.comment = comment;
        this.reviewTime = reviewTime;
    }
    public EntrySubmissionReview(int esid ,int admin, String status, String comment){
        this.esid = esid;
        this.admin = admin;
        this.status= status;
        this.comment = comment;
        this.reviewTime = LocalDateTime.now();
    }
    public EntrySubmissionReview(){
        this.esid = 0;
        this.status = "";
        this.comment = "";
        this.reviewTime = LocalDateTime.now();
    }
}
