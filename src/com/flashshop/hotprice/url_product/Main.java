package com.flashshop.hotprice.url_product;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 14.11.12
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private final static String DB_URL = "jdbc:mysql://flashs00.mysql.ukraine.com.ua/flashs00_db";
    private final static String USER = "flashs00_db";
    private final static String PASSWORD = "23111989kjpjd";

    public static String CATEGORY_NAME="Электронные книги";
    private static final String RESOURCE_FILE ="D:\\java\\jaxb-text\\resources\\productes" ;
    private static final String PRODUCT_URL_FILE ="D:\\java\\jaxb-text\\resources\\product_sku_url";



    public static void main(String[] args) throws Exception, IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(PRODUCT_URL_FILE));

        ProductSkuProvider provider = new CompositeProvider(new ProviderFromCategory(CATEGORY_NAME));

        for ( String productSku :provider.getSetOfProductSku()){
            Product product = new Product(productSku);
            URL url = new URL(getUrlConnection(product));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(false);
            con.getInputStream();
            product.addUrlOfProductToDataBase(con.getHeaderField("Location"));

            printWriter.println("Article "+productSku+" has a url "+product.getProductUrl());
        }

        printWriter.close();
    }

    public  static String getUrlConnection(Product product) throws SQLException {
        return "http://flashshop.com.ua/index.php?page=shop.product_details&flypage=flypage.tpl&product_id="+
                product.getProductId()+"&category_id="+product.getCategoryId()+"&option=com_virtuemart&Itemid=2";
    }
}
