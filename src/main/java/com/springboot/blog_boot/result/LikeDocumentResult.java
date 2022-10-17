package com.springboot.blog_boot.result;

public class LikeDocumentResult {
    private String action;
    private int seq;
    private String id;
    private int result;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public LikeDocumentResult(String action, int seq, String id, int result) {
        this.action = action;
        this.seq = seq;
        this.id = id;
        this.result = result;
    }
}
