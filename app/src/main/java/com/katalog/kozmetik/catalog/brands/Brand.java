package com.katalog.kozmetik.catalog.brands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 23/11/2017.
 */

public class Brand {
    private String name;
    private String id;
    private int image;
    private String title;
    private String coverPageUrl;
    private List<Page> pageList;
    private int pageCount;
    private int order;

    public Brand(String id, String name, int image, String title, String coverPageUrl, int pageCount, int order) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.title = title;
        this.coverPageUrl = coverPageUrl;
        this.pageCount = pageCount;
        this.order = order;
        pageList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPageUrl() {
        return coverPageUrl;
    }

    public void setCoverPageUrl(String coverPageUrl) {
        this.coverPageUrl = coverPageUrl;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
