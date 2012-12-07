package com.flashshop;

import com.flashshop.managers.DataBaseManager;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 04.11.12
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class Actualizator {
    private static int MARGIN = 20;
    private static double INTEREST_MARGIN = 1.025;

    public static void main(String[] args) throws SQLException, IOException, SAXException, ParserConfigurationException {
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        Set<String> setProductSku = dataBaseManager.getSetOfProductSku();
        int countProduct = 0;
        OverseerActualizer overseer = new OverseerActualizer(Categories.getCategories());
        while (overseer.hasNext()) {
            Product product = overseer.next();
                if (setProductSku.contains(product.productSku)) {
                setProductSku.remove(product.productSku);
                //if (product.productAvailability)
                    dataBaseManager.changePriceBySku(product.productSku,getPriceWithMargin(product));

                if (Slave.SET_DESCRIPTION && !dataBaseManager.isExistDescription(product.productSku))
                    dataBaseManager.changeDescription(product.productSku,product.description);
                //dataBaseManager.changeWarranty(product.productSku,product.warranty);
                dataBaseManager.changeAvailabilityBySku(product.productSku,product.productAvailability);
                System.out.println("Handle product of SKU: "+product.productSku+". Product availability : " + product.productAvailability);
                countProduct++;
            }
        }
        System.out.println("Count handled product "+countProduct);
       for (String productSku: setProductSku)
        dataBaseManager.changeAvailabilityBySku(productSku,false);

    }
    private static String getPriceWithMargin(Product product) throws IOException {
        if (Double.parseDouble(product.productPrice) > 200 )
        return Math.round(INTEREST_MARGIN*Double.parseDouble(product.productPrice)+MARGIN)+"";

        return (Math.round(Double.parseDouble(product.productPrice)+ MARGIN) + "");
    }
}
