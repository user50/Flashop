package com.flashshop;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 17.10.12
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class Slave
{
    public static final boolean SET_DESCRIPTION = false;

    private String categoryName;
    private String manufacturer;
    private String url;
    private HtmlTable table;
    private WebClient webClient;

    private int index = 1;

    public Slave(String categoryName, String manufacturer, String url,WebClient webClient) throws IOException {
        this.categoryName = categoryName;
        this.manufacturer = manufacturer;
        this.url = url;
        this.table = getHtmlTable(webClient);
        this.webClient = webClient;

    }

    private HtmlTable getHtmlTable(WebClient webClient) throws IOException {
        HtmlPage page = webClient.getPage(url);
        HtmlForm changeParamsForm = null;
        for(HtmlForm form: page.getForms()) {
            if (form.getActionAttribute().equals("change_params.php"))
                changeParamsForm = form;

        }
        HtmlSelect select = changeParamsForm.getSelectByName("per_page");
        select.setSelectedAttribute(select.getOptionByValue("65535"),true);
        page = (HtmlPage)changeParamsForm.click();
        return (HtmlTable) page.getElementById("items");
    }

    private Product createProduct(HtmlTableRow row) throws IOException {
    //todo
        Product product = new Product();
        product.productSku = row.getCell(2).asText();
        product.productName = row.getCell(3).asText();

        if (SET_DESCRIPTION){
            String descriptionCode = row.getCell(3).getElementsByTagName("a").get(0).getAttribute("href").replace("about/","").replaceAll(" ","%20");
            product.description = getDescription(descriptionCode);
        }

        product.productPrice = row.getCell(5).asText().split("/")[1].trim().replace(",", ".");
        product.shortDescription = row.getCell(3).getElementsByTagName("a").get(0).getAttribute("title");
        product.warranty = row.getCell(6).asText();
        product.categoryName = categoryName;
        product.manufacturer = manufacturer;

        List<HtmlElement> imgAvailability = (List<HtmlElement>) row.getCell(4).getElementsByTagName("img");
        if (imgAvailability.get(0).getAttribute("src").contains("null")){
            product.productAvailability = false;
        }
        else {
            product.productAvailability = true;
        }

        return product;
    }

    private String getDescription(String descriptionCode) throws IOException {
        String descriptionUrl = "http://kpi.com.ua/client/load2.ajax.php?action=loadInfo&id="+descriptionCode;

        URL url = new URL(descriptionUrl);
        HttpURLConnection con =  (HttpURLConnection) url.openConnection();
        con.setRequestProperty( "Cookie",webClient.getCookieManager().getCookies(url).toString().replace("[","").replace("]",""));
        InputStream inputStream = con.getInputStream();
        byte[] bytes = new byte[1];
        StringBuffer stringBuffer = new StringBuffer();
        while (inputStream.read(bytes)!=-1){
            stringBuffer.append(new String(bytes, "Cp1251"));
        }
        inputStream.close();
        return stringBuffer.toString().replaceAll("\\\\","").replaceAll("'","\"").
                replace("addInfo(\"","").replace("\");","").replace("item_descr","desctable");
    }


    public static byte[] convertEncoding(byte[] bytes, String from, String to) throws UnsupportedEncodingException {
        return new String(bytes, from).getBytes(to);
    }

    public boolean hasNext()
    {
        return index < table.getRows().size() && table.getRows().size()>1;
    }

    public Product next() throws IOException {
        return createProduct( table.getRow(index++) );
    }
}
