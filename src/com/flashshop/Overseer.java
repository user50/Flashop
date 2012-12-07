package com.flashshop;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: neuser50
 * Date: 17.10.12
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
public class Overseer
{
    private List<Category> categories;

    private int categoryIndex = 0;
    private int manufacturerIndex = 0;

    private Slave slave;
    private static final String MAIN_PAGE = "http://kpiservice.com.ua/" ;
    private WebClient webClient;

    public Overseer(List<Category> categories) throws IOException {
        this.categories = categories;
        this.webClient = new WebClient();
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        login();
        refreshSlave();

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

    public boolean hasNext()
    {
        if( categoryIndex >= categories.size() - 1 &&
            manufacturerIndex >= categories.get(categoryIndex).getManufacturers().size() &&
            !slave.hasNext()    )
            return false;

        return true;
    }

    public Product next() throws IOException {
        while( !slave.hasNext())
            refreshSlave();

        return slave.next();
    }

    public void refreshSlave() throws IOException {
        if(manufacturerIndex  >= categories.get(categoryIndex).getManufacturers().size() )
        {
            manufacturerIndex = 0;
            categoryIndex++;
        }

        Category category = categories.get(categoryIndex);
        String manufacturer = category.getManufacturers().get(manufacturerIndex++);

        slave = new Slave(category.getName(),
                manufacturer,
                category.getUrl(manufacturer), webClient);
        System.out.println("Get to category: "+category.getName()+". Get to manufacturer "+manufacturer);
    }

}
