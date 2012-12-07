package com.flashshop.hotprice.url_product;

import com.flashshop.managers.DataBaseManager;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 15.11.12
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class ProviderFromCategory implements ProductSkuProvider {
    
    private String categoryName;

    public ProviderFromCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public Set<String> getSetOfProductSku() throws Exception {
        try {
            DataBaseManager dataBaseManager = DataBaseManager.getInstance();
            Set<String> setOfProductSku = dataBaseManager.getProductSkuForCategory(categoryName);

            return setOfProductSku;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
