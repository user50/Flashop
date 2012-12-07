package com.flashshop.hotprice.url_product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 15.11.12
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class ProviderFromFile implements ProductSkuProvider {

    String resourceFile;

    public ProviderFromFile(String resourceFile) {
        this.resourceFile = resourceFile;
    }

    @Override
    public Set<String> getSetOfProductSku() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(resourceFile));
        Set<String> setOfProductSku = new HashSet<String>();

        String productSku;
        while ((productSku = bufferedReader.readLine()) != null)
            setOfProductSku.add(productSku);

        bufferedReader.close();
        return setOfProductSku;
    }
}
