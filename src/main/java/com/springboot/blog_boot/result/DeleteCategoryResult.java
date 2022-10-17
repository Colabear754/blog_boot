package com.springboot.blog_boot.result;

public class DeleteCategoryResult {
    private int category_id;
    private boolean result;

    public DeleteCategoryResult(int category_id, boolean result) {
        this.category_id = category_id;
        this.result = result;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
