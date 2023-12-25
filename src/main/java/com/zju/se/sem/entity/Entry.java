package com.zju.se.sem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Entry {
    private int eid;
    private String title;
    private String content;
    private String description;
    private String category;
    private String image;
    private int visits;
    private int likes;
    private int favors;

    public Entry(int eid, String title, String content, String description, String category, String image, int visits, int likes, int favors) {
        this.eid = eid;
        this.title = title;
        this.content = content;
        this.description = description;
        this.category = category;
        this.image = image;
        this.visits = visits;
        this.likes = likes;
        this.favors = favors;
    }

    public Entry(EntrySubmission entrySubmission){
        this.eid = entrySubmission.getEid();
        this.title = entrySubmission.getTitle();
        this.content = entrySubmission.getContent();
        this.description = entrySubmission.getDescription();
        this.category = entrySubmission.getCategory();
        this.image = entrySubmission.getImage();
        this.visits = 0;
        this.likes = 0;
        this.favors = 0;
    }
    public Entry() {
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFavors() {
        return favors;
    }

    public void setFavors(int favors) {
        this.favors = favors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return eid == entry.eid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid);
    }
}
