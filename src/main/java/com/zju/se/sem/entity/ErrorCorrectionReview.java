package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("error_correction_review")
public class ErrorCorrectionReview {
    @TableId(type = IdType.AUTO)
    private int ecrid;
    private int admin;
    private int ecid;
    private String status;
    private String comment;
    private LocalDateTime reviewTime;

    public ErrorCorrectionReview(int ecrid, int admin, int ecid, String status, String comment, LocalDateTime reviewTime) {
        this.ecrid = ecrid;
        this.admin = admin;
        this.ecid = ecid;
        this.status = status;
        this.comment = comment;
        this.reviewTime = reviewTime;
    }

    public ErrorCorrectionReview(int admin, int ecid, String status, String comment ){
        this.admin = admin;
        this.ecid = ecid;
        this.status = status;
        this.comment = comment;
        this.reviewTime = LocalDateTime.now();
    }
    public ErrorCorrectionReview(){
        this.ecid = 0;
        this.status = "";
        this.comment = "";
        this.reviewTime = LocalDateTime.now();
    }
}
