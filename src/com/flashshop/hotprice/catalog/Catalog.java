package com.flashshop.hotprice.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 10.11.12
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public class Catalog {

    public static int count = 1;
    public static HashMap<String,Category> categoryHashMap = new HashMap<String, Category>();

    public static String getManufacturerId(String categoryName, String manufacturerName){
        if (!categoryHashMap.keySet().contains(categoryName))
            categoryHashMap.put(categoryName,new Category(categoryName));

        if (!categoryHashMap.get(categoryName).containsManufacturer(manufacturerName))
            categoryHashMap.get(categoryName).addManufacturer(manufacturerName);

        return categoryHashMap.get(categoryName).manufacturersMap.get(manufacturerName).id;
    }

    public static List<com.flashshop.hotprice.model.Category>   getCategories(){
        List<com.flashshop.hotprice.model.Category> listCategories = new ArrayList<com.flashshop.hotprice.model.Category>();
        for(Category category: categoryHashMap.values()){
            listCategories.add(new com.flashshop.hotprice.model.Category(category.id, category.name));
            for(Manufacturer manufacturer: category.manufacturersMap.values())
                listCategories.add(new com.flashshop.hotprice.model.Category(manufacturer.id, category.id, manufacturer.name));

        }

        return listCategories;
    }

    private static class Manufacturer{

        String name;
        String id;

        private Manufacturer(String name) {
            this.name = name;
            this.id = (count++)+"";
        }
    }

    private  static  class Category{

        String id;
        String name;
        Map<String,Manufacturer> manufacturersMap = new HashMap<String, Manufacturer>();

        private Category(String name) {
            this.name = name;
            this.id = (count++)+"";
        }

        public void addManufacturer(String manufacturerName){
           manufacturersMap.put(manufacturerName,new Manufacturer(manufacturerName));
        }

        public boolean containsManufacturer(String manufacturerName){
            return manufacturersMap.keySet().contains(manufacturerName);
        }
    }


}
