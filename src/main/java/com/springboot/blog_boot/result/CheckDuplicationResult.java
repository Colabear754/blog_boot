package com.springboot.blog_boot.result;

public class CheckDuplicationResult {
    private String id;
    private boolean isDuplicated;

    public CheckDuplicationResult(String id, boolean isDuplicated) {
        this.id = id;
        this.isDuplicated = isDuplicated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDuplicated() {
        return isDuplicated;
    }

    public void setDuplicated(boolean duplicated) {
        isDuplicated = duplicated;
    }
}
