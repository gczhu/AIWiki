package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author 祝广程
 * @version 1.0
 */
public class UserEntry {
    @TableId(type = IdType.AUTO)
    private int ueid;
    private int uid;
    private int eid;

    public UserEntry(int ueid, int uid, int eid) {
        this.ueid = ueid;
        this.uid = uid;
        this.eid = eid;
    }

    public int getUeid() {
        return ueid;
    }

    public void setUeid(int ueid) {
        this.ueid = ueid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    @Override
    public String toString() {
        return "UserEntry{" +
                "ueid=" + ueid +
                ", uid=" + uid +
                ", eid=" + eid +
                '}';
    }
}
