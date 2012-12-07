package com.flashshop.hotprice.url_product;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 15.11.12
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class CompositeProvider implements ProductSkuProvider {

    List<ProductSkuProvider> providers = new ArrayList<ProductSkuProvider>();

    public CompositeProvider(ProductSkuProvider ... providers) {
        this.providers.addAll(Arrays.asList(providers));
    }

    @Override
    public Set<String> getSetOfProductSku() throws Exception {
        Set<String> setOfProductSku = new HashSet<String>();
        for (ProductSkuProvider provider: providers){
            setOfProductSku.addAll(provider.getSetOfProductSku());
        }

        return setOfProductSku;
    }
}
