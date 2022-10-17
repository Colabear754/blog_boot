package com.springboot.blog_boot.result;

public class WithdrawalResult {
    private String id;
    private boolean result;

    public WithdrawalResult(String id, boolean result) {
        this.id = id;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
