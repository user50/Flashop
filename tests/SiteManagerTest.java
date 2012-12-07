import com.flashshop.hotprice.url_product.CompositeProvider;
import com.flashshop.hotprice.url_product.Product;
import com.flashshop.hotprice.url_product.ProductSkuProvider;
import com.flashshop.hotprice.url_product.ProviderFromCategory;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.flashshop.managers.SiteManager;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 14.08.12
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public class SiteManagerTest {

    @Test
    public void blyadskaHuinia() {
        String date = "Thu, 15 Aug 2013 19:21:16 GMT";
        DateFormat expiresFormat2 = new SimpleDateFormat("'Thu', dd 'Aug' yyyy HH:mm:ss z");

        try {
            expiresFormat2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        SiteManager manager = new SiteManager();
        manager.login();
        manager.getAllPage("75803");
        System.out.println(manager.allPage);
    }

    @Test
    public void isProductExistlTest() throws IOException, InterruptedException {
        SiteManager manager = new SiteManager();
        manager.login();
        manager.getAllPage("75803");

        if(manager.isProductExist()){
            System.out.println(manager.getProductPrice());
        }
    }

    @Test
    public void getProductUrlTest() throws Exception {
        WebClient webClient = new WebClient();

        HtmlPage page1 = webClient.getPage("http://kpi.com.ua/");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        HtmlForm form = (HtmlForm)page1.getElementById("log_in_form");


        HtmlSubmitInput button = form.getInputByValue("Войти");
        HtmlTextInput textField = form.getInputByName("fusername");

        // Change the value of the text field
        textField.setValueAttribute("26011");


        HtmlPasswordInput pass = form.getInputByName("fpassword");
        pass.setValueAttribute("neuser50mart");

        // Now submit the form by clicking the button and get back the second page.
        HtmlPage page2 = button.click();

        String mainPage = "http://kpi.com.ua/client/load2.ajax.php?action=loadInfo&id=%20%2017BQSRV";


        HtmlPage page = webClient.getPage(mainPage);

        System.out.println(page.asText());
        int i =0;
//        HtmlForm form = (HtmlForm) page.getElementById("search");
//        HtmlSubmitInput button = form.getInputByName("Search");
//        HtmlTextInput textField = form.getInputByName("keyword");
//
//        textField.setValueAttribute("14276");
//        HtmlPage productPage = button.click();
//
//        productPage.getUrl();
    }

    @Test
    public void testName() throws Exception {
        URL url = new URL("http://flashshop.com.ua/index.php?page=shop.product_details&flypage=flypage.tpl&product_id=1220&category_id=11&option=com_virtuemart&Itemid=2") ;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(false);
        con.getInputStream();
        String productUrl = new String(con.getHeaderField("Location"));
        String[] strings = productUrl.split("/");
        String categoryEncode = URLEncoder.encode("Мышки","UTF-8");
        strings[3]=categoryEncode;
        productUrl = "http://";
        for (int i = 1; i < strings.length;i++)
            productUrl = productUrl+strings[i]+"/";
        System.out.println(productUrl);
    }

    @Test
    public void testName1() throws Exception {
        URL url = new URL("http://kpi.com.ua/client/load2.ajax.php?action=loadInfo&id=%20%2017BQSRV&1353447694886");
        HttpURLConnection con =  (HttpURLConnection) url.openConnection();
        InputStream inputStream = con.getInputStream();
        FileOutputStream outputStream = new FileOutputStream("D:\\java\\db_site\\tests\\resourse\\description");
        byte[] bytes = new byte[1];

        while (inputStream.read(bytes)!=-1)
            outputStream.write(bytes);

        outputStream.close();;
        inputStream.close();
    }

    @Test
    public void testName2() throws Exception {
        String productSku = "96654";
        Product product = new Product(productSku);
        URL url = new URL(getUrlConnection(product));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(false);
        con.getInputStream();
        product.addUrlOfProductToDataBase(con.getHeaderField("Location"));

        for(byte b: con.getHeaderField("Location").getBytes())
            System.out.println(b);


    }

    @Test
    public void testNameff() throws Exception {
        char c = '\u0424';
        new String(c+"").getBytes();

    }

    public  static String getUrlConnection(Product product) throws SQLException {
        return "http://flashshop.com.ua/index.php?page=shop.product_details&flypage=flypage.tpl&product_id="+
                product.getProductId()+"&category_id="+product.getCategoryId()+"&option=com_virtuemart&Itemid=2";
    }
}
