package com.flashshop;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 17.10.12
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class Category
{
    private String url;
    private String name;
    private List<String> manufacturers;
    private String categoryUrl;

    public Category(String name,String categoryUrl, String url ,List<String> manufacturers) {
        this.name = name;
        this.manufacturers = manufacturers;
        this.url = url;
        this.categoryUrl = categoryUrl;
    }

    public String getName() {
        return name;
    }

    public List<String> getManufacturers() {
        return manufacturers;
    }

    public String getUrl(String manufacturerName)
    {
        return url+manufacturerName;
    }

    public String getCategoryUrl(){
        return categoryUrl;
    }
}

