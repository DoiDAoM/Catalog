package com.katalog.kozmetik.catalog.brands;

/**
 * Created by user on 9.12.2017.
 */

public class Page {
    private String id;
    private String url;
    private boolean isFavourite;

    public Page(String id, String url, boolean isFavourite) {
        this.id = id;
        this.url = url;
        this.isFavourite = isFavourite;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

}
