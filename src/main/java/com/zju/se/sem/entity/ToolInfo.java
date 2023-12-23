package com.zju.se.sem.entity;

/**
 * @author 祝广程
 * @version 1.0
 */
public class ToolInfo {
    private int id;
    private String title;
    private String href;
    private String description;
    private String category;

    public ToolInfo(int id, String title, String href, String description, String category) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.description = description;
        this.category = category;
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

    @Override
    public String toString() {
        return "ToolInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", href='" + href + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
