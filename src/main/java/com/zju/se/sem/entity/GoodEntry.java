package com.zju.se.sem.entity;

import java.util.Objects;

/**
 * @author 祝广程
 * @version 1.0
 */
public class GoodEntry {
    private int id;
    private String title;

    public GoodEntry(int id, String title) {
        this.id = id;
        this.title = title;
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

    @Override
    public String toString() {
        return "GoodEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodEntry goodEntry = (GoodEntry) o;
        return id == goodEntry.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
