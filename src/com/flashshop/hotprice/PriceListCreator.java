package com.flashshop.hotprice;

import com.flashshop.hotprice.catalog.Catalog;
import com.flashshop.hotprice.model.Item;
import com.flashshop.hotprice.model.Price;
import com.flashshop.hotprice.url_product.CompositeProvider;
import com.flashshop.hotprice.url_product.ProductSkuProvider;
import com.flashshop.hotprice.url_product.ProviderFromCategory;
import com.flashshop.hotprice.url_product.ProviderFromFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 11.11.12
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class PriceListCreator {
    public final static String DB_URL = "jdbc:mysql://flashs00.mysql.ukraine.com.ua/flashs00_db";
    public final static String USER = "flashs00_db";
    public final static String PASSWORD = "23111989kjpjd";
    public final static String RESOURCE_FILE = "D:\\java\\jaxb-text\\resources\\productes";
    public final static String FLASHSHOP_PRICE_LIST = "D:\\java\\jaxb-text\\resources\\flashshop_price_list.xml";
    public final static String URL_IMAGE_CATALOG="http://flashshop.com.ua/components/com_virtuemart/shop_image/product/";
    public  static Statement s;

    static {
        Connection c = null;
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            s = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception, SQLException, JAXBException {
        ProductSkuProvider provider = new CompositeProvider(new ProviderFromCategory("Наушники"),
                                                            new ProviderFromCategory("Флешки"),
                                                            new ProviderFromCategory("Электронные книги"),
                                                            new ProviderFromFile(RESOURCE_FILE)
                                                            );

        Price price = new Price();

        for (String productSku: provider.getSetOfProductSku()){
            Item item = getAvailableItem(productSku);
            if (item!=null)
                price.addItem(item);
        }
        price.setCatalog(Catalog.getCategories());
        save(price) ;
    }

    public static Item getAvailableItem(String productSku) throws SQLException {

        String query = String.format("call get_product_for_hotprice('"+productSku+"')");
        ResultSet resultSet = s.executeQuery(query);
        if (!resultSet.next()){
            throw new  RuntimeException("Product with product_sku  '"+productSku+"' does not exist.");
        }

        System.out.println(resultSet.getString("product_in_stock")+" "+productSku);

        if (Integer.parseInt(resultSet.getString("product_in_stock"))!=5)
            return null;

        String s;

        Item item = new Item();
        item.setId(resultSet.getString("product_id"));
        item.setName( resultSet.getString("product_name") );
        item.setUrl(resultSet.getString("product_url_ok"));
        item.setPrice(Double.parseDouble(resultSet.getString("product_price")));
        item.setCategoryId(Catalog.getManufacturerId(resultSet.getString("category_name"), resultSet.getString("mf_name")));
        item.setVendor(resultSet.getString("mf_name"));
        item.setImage(URL_IMAGE_CATALOG+resultSet.getString("product_full_image"));
        item.setDescription(resultSet.getString("product_s_desc"));

        return item;
    }

    public static  void save(Price price) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance( Price.class );

        // marshall into XML via System.out
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1251");
        marshaller.marshal( price, new FileOutputStream(FLASHSHOP_PRICE_LIST));
    }
}
