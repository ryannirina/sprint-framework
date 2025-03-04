package com.sprintframework.web.modelview;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String url;
    private Map<String, Object> data;

    public ModelView(String url) {
        this.url = url;
        this.data = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addItem(String key, Object value) {
        this.data.put(key, value);
    }
}
