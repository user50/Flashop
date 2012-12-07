package com.flashshop.managers;

import com.flashshop.Product;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseManager {

    private static DataBaseManager ourInstance = new DataBaseManager();

    private final static String DB_URL = "jdbc:mysql://flashs00.mysql.ukraine.com.ua/flashs00_db";
    private final static String USER = "flashs00_db";
    private final static String PASSWORD = "23111989kjpjd";

    private Statement s;


    public static DataBaseManager getInstance() {
        return ourInstance;
    }

    private DataBaseManager() {
        try {
            Connection c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            s = c.createStatement();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

    }

    public void addTradePosition(Product product) throws SQLException {
        String query = String.format("call add_trade_position('%s','%s','%s','%s','%s','%s','%s')",
                product.categoryName,product.manufacturer,product.productName,
                product.productSku,product.productPrice,product.shortDescription,product.description);
        s.execute(query);
    }
    public void changePrice(String id, String price) throws SQLException {
        String query = "UPDATE `jos_vm_product_price` SET `product_price` = '"+price+"' WHERE `jos_vm_product_price`.`product_id` = "+id+";";
        s.executeUpdate(query);
    }
    public void changePriceBySku(String productSku, String price) throws SQLException {
        String query = "UPDATE `jos_vm_product_price` SET `product_price` = '"+price+"' WHERE `product_id`=(SELECT product_id FROM jos_vm_product WHERE product_sku='"+productSku+"');";
        s.executeUpdate(query);
    }

    public ArrayList<String> getListOfProductId() throws SQLException {
        ArrayList<String> list = new ArrayList<String>();
        String query = "SELECT product_id FROM `jos_vm_product`";
        ResultSet r = s.executeQuery(query);
        while (r.next()) {
            list.add(r.getString("product_id"));
        }
        return list;
    }

    public String getProductSku(String id) throws SQLException {
        String query = "SELECT `product_sku` FROM `jos_vm_product` WHERE `product_id`="+id;
        ResultSet r = s.executeQuery(query);
        r.next();
        return r.getString("product_sku");

    }

    public void changeQuantityInStock(String id, String count) throws SQLException {
        String query = "UPDATE `jos_vm_product` SET `product_in_stock` = '"+count+"' WHERE `jos_vm_product`.`product_id` ="+id+";";
        s.executeUpdate(query);
    }

    public Set<String> getSetOfProductSku() throws SQLException {
        String query = "SELECT `product_sku` FROM `jos_vm_product`";
        ResultSet r = s.executeQuery(query);
        Set<String> setOfProductSku = new HashSet<String>();
        while (r.next())
            setOfProductSku.add(r.getString("product_sku"));

        return setOfProductSku;
    }

    public void changeAvailabilityBySku(String productSku, boolean productAvailability) throws SQLException {
        String quantityInStock;
        if(productAvailability)
            quantityInStock = "5";
        else
            quantityInStock = "0";

        String query = "UPDATE `jos_vm_product` SET `product_in_stock` = '"+quantityInStock+"' WHERE `product_sku` ='"+productSku+"';";
        s.executeUpdate(query);
    }

    public Set<String> getProductSkuForCategory(String categoryName) throws SQLException {
        String query = "SELECT product_sku FROM jos_vm_product " +
                "JOIN jos_vm_product_category_xref " +
                "ON jos_vm_product_category_xref.product_id=jos_vm_product.product_id " +
                "JOIN jos_vm_category " +
                "ON jos_vm_category.category_id=jos_vm_product_category_xref.category_id " +
                "WHERE category_name='"+categoryName+"'";

        ResultSet r = s.executeQuery(query);
        Set<String> setOfProductSku = new HashSet<String>();
        while (r.next()){
            setOfProductSku.add(r.getString("product_sku"));
        }
        return  setOfProductSku;
    }
    public void addUrlOfProductToDataBase(String productSku, String productUrl) throws SQLException, UnsupportedEncodingException {

        String query = "UPDATE `jos_vm_product` SET `product_url_ok`='"+productUrl+"' WHERE `product_sku`='"+productSku+"';";
        s.executeUpdate(query);
    }



    public void close() throws SQLException {
        s.close();
    }

    public void changeDescription(String productSku, String description) throws SQLException {
        String query = "UPDATE `jos_vm_product` SET `product_desc`='"+description+"' WHERE `product_sku`='"+productSku+"';";
        s.executeUpdate(query);

    }

    public void changeWarranty(String product_sku, String productWarranty) throws SQLException {
        String query = "UPDATE `jos_vm_product` SET `product_warranty`='"+productWarranty+"' WHERE `product_sku`='"+product_sku+"';";
        s.executeUpdate(query);
    }

    public boolean isExistDescription(String productSku) throws SQLException {
        String query = "SELECT product_desc FROM jos_vm_product WHERE product_sku='"+productSku+"';";
        ResultSet resultSet = s.executeQuery(query);
        if (!resultSet.next())
            return  false;
        String description = resultSet.getString("product_desc");
        return !description.isEmpty();
    }
}
