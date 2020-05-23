package com.mulganov.test_task.nix.model.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mulganov.test_task.nix.model.list.Info;

@Entity()
public class Element {
    @PrimaryKey(autoGenerate = true)
    private int key;

    private String text;
    private String img;
    private boolean kyplen;

    public Element(){}

    public Element(Info info){
        text = info.text;
        img = info.file;
        kyplen = info.kyplen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isKyplen() {
        return kyplen;
    }

    public void setKyplen(boolean kyplen) {
        this.kyplen = kyplen;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
