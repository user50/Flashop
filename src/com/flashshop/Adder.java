package com.flashshop;

import com.flashshop.Categories;
import com.flashshop.Overseer;
import com.flashshop.Product;
import com.flashshop.managers.DataBaseManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 18.10.12
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class Adder {
    public static void main(String[] args) throws IOException, SQLException, SAXException, ParserConfigurationException {

        Product p;
        Overseer overseer = new Overseer(Categories.getCategories());
        while (overseer.hasNext()) {
            Product product = overseer.next();
            //if (!isBannProduct(product,new String[]{"Вентилятор"})) {
                modifyProductName(product,"Вентилятор ","");
                DataBaseManager.getInstance().addTradePosition(product);
                System.out.println("Product has been handled: "+product);
            //}
        }

    }

    private static void modifyProductName(Product product, String editableText, String newText ){
        product.productName = product.productName.replace(editableText,newText);
    }
    public static boolean isBannProduct(Product product, String[] bannedWords){
        for (int  i=0; i<bannedWords.length; i++){
        if (product.productName.contains(bannedWords[i]))
            return true;
        }
        return false;
    }

}
