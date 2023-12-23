package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author 祝广程
 * @version 1.0
 */
public class EntryTag {
    @TableId(type = IdType.AUTO)
    private int etid;
    private int eid;
    private String tag;

    public EntryTag(int etid, int eid, String tag) {
        this.etid = etid;
        this.eid = eid;
        this.tag = tag;
    }

    public int getEtid() {
        return etid;
    }

    public void setEtid(int etid) {
        this.etid = etid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "EntryTag{" +
                "etid=" + etid +
                ", eid=" + eid +
                ", tag='" + tag + '\'' +
                '}';
    }
}
