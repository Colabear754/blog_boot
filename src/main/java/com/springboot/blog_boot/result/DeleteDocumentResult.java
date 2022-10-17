package com.springboot.blog_boot.result;

public class DeleteDocumentResult {
    private int seq;
    private boolean result;

    public DeleteDocumentResult(int seq, boolean result) {
        this.seq = seq;
        this.result = result;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
