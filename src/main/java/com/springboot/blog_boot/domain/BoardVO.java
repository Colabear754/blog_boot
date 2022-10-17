package com.example.blog.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public class BoardVO {
    private int seq;
    private Timestamp reg_time;
    private int category_id;
    private String subject;
    private String content;
    private String thumbnail;
    private int view_cnt;
    private int like_cnt;

    public BoardVO() {
        super();
    }

    public BoardVO(int seq, Timestamp reg_time, int category_id, String subject, String content, String thumbnail, int view_cnt, int like_cnt) {
        this.seq = seq;
        this.reg_time = reg_time;
        this.category_id = category_id;
        this.subject = subject;
        this.content = content;
        this.thumbnail = thumbnail;
        this.view_cnt = view_cnt;
        this.like_cnt = like_cnt;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Timestamp getReg_time() {
        return reg_time;
    }

    public void setReg_time(Timestamp reg_time) {
        this.reg_time = reg_time;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(int view_cnt) {
        this.view_cnt = view_cnt;
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(int like_cnt) {
        this.like_cnt = like_cnt;
    }
}
