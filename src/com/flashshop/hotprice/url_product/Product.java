package com.flashshop.hotprice.url_product;

import com.flashshop.managers.DataBaseManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 14.11.12
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class Product {
    private final static String DB_URL = "jdbc:mysql://flashs00.mysql.ukraine.com.ua/flashs00_db";
    private final static String USER = "flashs00_db";
    private final static String PASSWORD = "23111989kjpjd";

    private String productId;
    private String productSku;
    private String categoryId;
    private String categoryName;
    private String productUrl = "http:/";

    public  static Statement s;

    public Product(String productSku) throws SQLException {
        this.productSku = productSku;
        Connection c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        s = c.createStatement();
        String query = String.format("call get_product_cat('"+productSku+"')");
        ResultSet resultSet = s.executeQuery(query);
        if (!resultSet.next()){
            throw new  RuntimeException("Product with product_sku  '"+productSku+"' does not exist.");
        }

        setProductId(resultSet.getString("product_id"));
        setCategoryId(resultSet.getString("category_id"));
        setCategoryName(resultSet.getString("category_name"));

        s.close();
    }

    public void addUrlOfProductToDataBase(String productUrl) throws SQLException, UnsupportedEncodingException {
        String[] stringsUrl = productUrl.split("/");
        String categoryEncode = URLEncoder.encode(categoryName, "UTF-8");
        stringsUrl[3] = categoryEncode;
        stringsUrl[3] = categoryEncode.replace("+","-");

        for (int i = 1; i < stringsUrl.length;i++)
            this.productUrl = this.productUrl+stringsUrl[i]+"/";

        DataBaseManager.getInstance().addUrlOfProductToDataBase(productSku, this.productUrl);

    }


    public String getProductId() {
        return productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
