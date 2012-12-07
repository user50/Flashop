package com.flashshop.hotprice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 09.11.12
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Currency {
    @XmlAttribute
    private  String code = "UAH";

    public String getCode() {
        return code;
    }
}
