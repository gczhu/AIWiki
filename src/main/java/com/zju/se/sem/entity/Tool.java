package com.zju.se.sem.entity;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Tool {
    private int tid;
    private String title;
    private String href;
    private String url;
    private String description;
    private String category;
    private int recommend;

    public Tool(int tid, String title, String href, String url, String description, String category, int recommend) {
        this.tid = tid;
        this.title = title;
        this.href = href;
        this.url = url;
        this.description = description;
        this.category = category;
        this.recommend = recommend;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
