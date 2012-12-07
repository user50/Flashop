package com.flashshop.managers;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Evgen
 * Date: 13.08.12
 * Time: 23:41
 * To change this template use File | Settings | File Templates.
 */
public class SiteManager {

    public final static String SUPPLIER_URL = "http://kpiservice.com.ua/search/tabled/";
    public final static String CODE_PAGE = "windows-1251";
    private final static String MAIN_PAGE = "http://kpiservice.com.ua/";

    public HtmlPage allPage;
//    private String cookie = "";
    private WebClient webClient;

    private boolean availability;
    private String price;


    public SiteManager() {
        webClient = new WebClient();

        // Отключаем отображение ошибок
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        // Используем движок Firefox 3.6 (он совместим с большинством js-библиотек)
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);

        // Ждем, пока отработают ajax-запросы, выполняемые при загрузке страницы
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    public void getAllPage(String product_sku) throws IOException {

        allPage = null;


        // Запрашиваем веб-страницу
        HtmlPage page = webClient.getPage(MAIN_PAGE);

        HtmlForm form = (HtmlForm) page.getElementById("s_form");
        HtmlSubmitInput button = form.getInputByValue("Найти");
        HtmlTextInput textField = form.getInputByName("search");
        textField.setValueAttribute(product_sku);

        allPage = button.click();

        processPage();
        webClient.closeAllWindows();

        /*URL pageURL = new URL(SUPPLIER_URL);
        HttpURLConnection uc = (HttpURLConnection)pageURL.openConnection();
        uc.setRequestMethod("POST");
        uc.setDoOutput(true);
       // if (!cookie.equals(""))
          //  uc.setRequestProperty("Cookie",cookie);
        String query = "search="+product_sku+"&new_search=1";
        uc.getOutputStream().write(query.getBytes());
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        uc.getInputStream(),CODE_PAGE));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            allPage = allPage+inputLine+"\n";
        }
        br.close(); */
    }

    public String getProductPrice() throws IOException {
        return  price;
    }

    public void processPage() throws IOException {

        HtmlTable table = (HtmlTable)allPage.getElementById("items");
        List<HtmlTableBody> list = table.getBodies();

        HtmlTableBody tableBody = list.get(0);
        List<HtmlElement> tdElements = tableBody.getHtmlElementsByTagName("td");

        if(!tdElements.isEmpty()){
            List<HtmlElement> imgNotify = tdElements.get(5).getHtmlElementsByTagName("img");
            if (imgNotify.get(0).getAttribute("src").contains("null")){
            availability = false;
            return;
        }
            availability = true;
        }
        else {
            availability = false;
            return;
        }



        String[] prices = tdElements.get(6).asText().split("/");
        price = prices[prices.length-1].trim();
        price = price.split(",")[0];



    }


    public void login() throws IOException{

        // Get the first page
        HtmlPage page1 = webClient.getPage(MAIN_PAGE);

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
        HtmlPage mainPage = button.click();


    }

    public  boolean isProductExist(){
        return availability;
    }


}
