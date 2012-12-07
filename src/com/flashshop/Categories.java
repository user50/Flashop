package com.flashshop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 17.10.12
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class Categories {

    public final static String CONFIG_FILE = "D:\\java\\db_site\\tests\\resourse\\categories";

    public static List<Category> getCategories() throws IOException, SAXException, ParserConfigurationException {
        //todo
        DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
        builderfactory.setNamespaceAware(true);

        DocumentBuilder builder = builderfactory.newDocumentBuilder();
        Document xmlDocument = (Document) builder.parse(CONFIG_FILE);
        NodeList categorieNodes = xmlDocument.getElementsByTagName("category");
        List<Category> categories = new ArrayList<Category>();
        for (int i=0; i<categorieNodes.getLength(); i++ ){
              categories.add(createCategory((Element)categorieNodes.item(i)));

        }

        return categories;
    }
        private static Category createCategory(Element categoryNode) {

            String name = categoryNode.getAttribute("name");
            String url = categoryNode.getAttribute("url");
            String categoryUrl = categoryNode.getAttribute("categoryUrl");
            NodeList manufacturerNodes = categoryNode.getElementsByTagName("manufacturer");
            ArrayList<String> manufacturers = new ArrayList<String>();
            for (int i=0; i<manufacturerNodes.getLength(); i++){
                manufacturers.add(manufacturerNodes.item(i).getTextContent());
            }
           return new Category(name,categoryUrl,url,manufacturers);

        }
//    public static  List<Category> getTestCategories(){
//
//        List<String> manufacturers = Arrays.asList("Transcend","GooDRAM","Kingston","CORSAIR");
//        Category category = new Category("Флешки","http://kpi.com.ua/category/tabled/48/55655//",manufacturers);
//        Category category2 = new Category("Flash-карты","http://kpi.com.ua/category/tabled/48/5996//",Arrays.asList("Transcend","Kingston","GooDRAM"));
//
//
//        return Arrays.asList(category,category2);
//    }
}
