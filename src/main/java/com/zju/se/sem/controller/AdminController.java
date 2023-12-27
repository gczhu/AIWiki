package com.zju.se.sem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zju.se.sem.entity.*;
import com.zju.se.sem.mapper.*;
import com.zju.se.sem.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {
    private UserMapper userMapper;
    private EntryMapper entryMapper;
    private EntrySubmissionMapper entrySubmissionMapper;
    private EntrySubmissionReviewMapper entrySubmissionReviewMapper;
    private ErrorCorrectionMapper errorCorrectionMapper;
    private ErrorCorrectionReviewMapper errorCorrectionReviewMapper;
    @Autowired
    public AdminController(UserMapper userMapper, EntryMapper entryMapper, EntrySubmissionMapper entrySubmissionMapper, EntrySubmissionReviewMapper entrySubmissionReviewMapper, ErrorCorrectionMapper errorCorrectionMapper, ErrorCorrectionReviewMapper errorCorrectionReviewMapper) {
        this.userMapper = userMapper;
        this.entryMapper = entryMapper;
        this.entrySubmissionMapper = entrySubmissionMapper;
        this.entrySubmissionReviewMapper = entrySubmissionReviewMapper;
        this.errorCorrectionMapper = errorCorrectionMapper;
        this.errorCorrectionReviewMapper = errorCorrectionReviewMapper;
    }
    private Boolean isAdmin(int uid){
        int role = userMapper.selectById(uid).getRole();
        return role == 1;
    }

    @PostMapping("/deleteEntry")
    public Message deleteEntry(
            @RequestHeader("Authorization") String token,
            @RequestBody Integer id) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            System.out.println("123");
            if (id == 0) {
                return new Message(false, "删除词条失败", 20001);
            }
            if(!isAdmin(uid)){
                return new Message(false, "无管理员权限", 20001);
            }

            LambdaQueryWrapper<Entry> entryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            entryLambdaQueryWrapper.eq(Entry::getEid, id);
            int res = entryMapper.delete(entryLambdaQueryWrapper);

            if (res > 0) {
                return new Message(true, "成功删除词条", 20000);
            } else {
                return new Message(false, "删除词条失败", 20001);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(false, "删除词条失败", 20001);
        }
    }

    @GetMapping("/queryEntrySubmission")
    public Message queryEntrySubmission(@RequestHeader("Authorization") String token) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if(!isAdmin(uid)){
                return new Message(false, "无管理员权限", 20001);
            }
            LambdaQueryWrapper<EntrySubmission> entrySubmissionQueryWrapper = new LambdaQueryWrapper<>();
            entrySubmissionQueryWrapper.eq(EntrySubmission::getStatus, "待处理");
            entrySubmissionQueryWrapper.eq(EntrySubmission::getAdmin, uid);
            List<EntrySubmission> entrySubmissions = entrySubmissionMapper.selectList(entrySubmissionQueryWrapper);
            return new Message(true, "查询用户添加词条成功", 20000).data("entrySubmissions", entrySubmissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(false, "查询用户添加词条失败", 20001);
        }
    }
    @PostMapping("/reviewEntrySubmission")
    public Message reviewEntrySubmission(@RequestHeader("Authorization") String token,
                                  @RequestParam(defaultValue = "0") int esid,
                                  @RequestParam(defaultValue = "") String status,
                                  @RequestParam(defaultValue = "") String comment) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if(!isAdmin(uid)){
                return new Message(false, "无管理员权限", 20001);
            }
            if(esid == 0){
                return new Message(false, "审核用户添加词条失败", 20001);
            }
            if(!status.equals("")){
                EntrySubmission entrySubmission = entrySubmissionMapper.selectById(esid);
                entrySubmission.setStatus(status);
                EntrySubmissionReview entrySubmissionReview = new EntrySubmissionReview(esid, uid, status, comment);
                LambdaQueryWrapper<EntrySubmission> entrySubmissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
                entrySubmissionLambdaQueryWrapper.eq(EntrySubmission::getEsid, entrySubmission.getEsid());
                int res = entrySubmissionMapper.update(entrySubmission, entrySubmissionLambdaQueryWrapper) & entrySubmissionReviewMapper.insert(entrySubmissionReview);
                if(res == 1){
                    if(entrySubmission.getStatus().equals("通过")){
                        Entry entry = new Entry(entrySubmission);
                        if(entryMapper.insert(entry) == 1)
                            return new Message(true, "审核信息上传成功，词条已添加", 20000);
                        else{
                            return new Message(false, "审核信息上传成功，词条添加失败", 20001);
                        }
                    }
                    return new Message(true, "审核信息上传成功", 20000);
                }
            }
            return new Message(false, "审核信息上传失败", 20001);
        } catch (Exception e) {
            return new Message(false, "审核信息上传失败", 20001);
        }
    }

    @GetMapping("/queryErrorCorrection")
    public Message queryErrorCorrection(@RequestHeader("Authorization") String token) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if(!isAdmin(uid)){
                return new Message(false, "无管理员权限", 20001);
            }
            LambdaQueryWrapper<ErrorCorrection> errorCorrectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            List<ErrorCorrection> errorCorrections = errorCorrectionMapper.selectList(errorCorrectionLambdaQueryWrapper);
            return new Message(true, "查询用户差错纠正成功", 20000).data("entrySubmissions", errorCorrections);
        } catch (Exception e) {
            return new Message(false, "查询用户差错纠正失败", 20001);
        }
    }
    @PostMapping("/reviewErrorCorrection")
    public Message reviewErrorCorrection(@RequestHeader("Authorization") String token,
                                         @RequestBody ErrorCorrectionReview errorCorrectionReview) {
        try {
            Claims claims;
            int uid;
            try {
                claims = JwtUtils.getClaimsByToken(token);
                uid = Integer.parseInt(claims.getSubject());
            } catch (Exception e) {
                return new Message(false, "token错误", 50001);
            }
            if(!isAdmin(uid)){
                return new Message(false, "无管理员权限", 20001);
            }
            if(errorCorrectionReview.getEcid() == 0){
                return new Message(false, "审核用户差错纠正失败", 20001);
            }
            if(!errorCorrectionReview.getStatus().equals("")){
                ErrorCorrection errorCorrection = errorCorrectionMapper.selectById(errorCorrectionReview.getEcid());
                errorCorrection.setStatus(errorCorrectionReview.getStatus());
                LambdaQueryWrapper<ErrorCorrection> errorCorrectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
                errorCorrectionLambdaQueryWrapper.eq(ErrorCorrection::getEcid, errorCorrection.getEcid());
                int res = errorCorrectionMapper.update(errorCorrection, errorCorrectionLambdaQueryWrapper) & errorCorrectionReviewMapper.insert(errorCorrectionReview);
                if(res == 1){
                    if(errorCorrection.getStatus().equals("通过")){
                        LambdaQueryWrapper<Entry> entryLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        entryLambdaQueryWrapper.eq(Entry::getEid, errorCorrection.getEid());
                        Entry entry = entryMapper.selectList(entryLambdaQueryWrapper).get(0);
                        if(errorCorrection.getTitle()!=null && !errorCorrection.getTitle().equals(""))
                            entry.setTitle(errorCorrection.getTitle());
                        if(errorCorrection.getContent()!=null &&!errorCorrection.getContent().equals(""))
                            entry.setContent(errorCorrection.getContent());
                        if(errorCorrection.getDescription()!=null &&!errorCorrection.getDescription().equals(""))
                            entry.setDescription(errorCorrection.getDescription());
                        if(errorCorrection.getCategory()!=null &&!errorCorrection.getCategory().equals(""))
                            entry.setCategory(errorCorrection.getCategory());
                        if(errorCorrection.getImage()!=null &&!errorCorrection.getImage().equals(""))
                            entry.setImage(errorCorrection.getImage());

                        if(entryMapper.update(entry, entryLambdaQueryWrapper) == 1)
                            return new Message(true, "审核信息上传成功，词条已修改", 20000);
                        else{
                            return new Message(false, "审核信息上传成功，词条修改失败", 20001);
                        }
                    }
                    return new Message(true, "审核信息上传成功", 20000);
                }
            }
            return new Message(false, "审核信息上传失败", 20001);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(false, "审核信息上传失败", 20001);
        }
    }
}
