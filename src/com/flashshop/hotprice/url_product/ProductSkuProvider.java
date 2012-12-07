package com.flashshop.hotprice.url_product;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 15.11.12
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public interface ProductSkuProvider {
    public Set<String> getSetOfProductSku() throws Exception;

}
