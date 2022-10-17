package com.example.blog.domain;

public class Like_documentVO {
    private String id;
    private int seq;

    public Like_documentVO() {
        super();
    }

    public Like_documentVO(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
