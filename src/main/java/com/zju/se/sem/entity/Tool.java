package com.zju.se.sem.entity;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Tool {
    private int tid;
    private String title;
    private String href;
    private String description;
    private String category;

    public Tool(int tid, String title, String href, String description, String category) {
        this.tid = tid;
        this.title = title;
        this.href = href;
        this.description = description;
        this.category = category;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

}
