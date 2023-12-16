package com.zju.se.sem.entity;

import java.util.List;

/**
 * @author 祝广程
 * @version 1.0
 */
public class EntryInfo {
    private int id;
    private String title;
    private String text;
    private List<GoodEntry> relation;

    public EntryInfo(int id, String title, String text, List<GoodEntry> relation) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.relation = relation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<GoodEntry> getRelation() {
        return relation;
    }

    public void setRelation(List<GoodEntry> relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "EntryInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", relation=" + relation +
                '}';
    }

}
