package ch.noseryoung.persistence;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Pendenz implements Serializable, Comparable<Pendenz> {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int priority;
    private long date;
    private String description;


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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Pendenz pendenz) {
        return Long.valueOf(date).compareTo(pendenz.getDate());
    }
}

