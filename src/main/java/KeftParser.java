import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KeftParser
{
    private final WebClient webClient;
    private String login;

    public KeftParser(WebClient webClient)
    {
        this.webClient = webClient;
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);

//        ProxyConfig proxyConfig = new ProxyConfig("proxyHost", proxyPort);
//        webClient.getOptions().setProxyConfig(proxyConfig);
    }

    public void auth() throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("логин: ");
        login = scanner.nextLine();
        System.out.println("пароль: ");
        String password = scanner.nextLine();

        HtmlPage page = webClient.getPage("https://www.keft.ru/");
        HtmlForm form = page.getFirstByXPath("/html/body/table/tbody/tr/td[2]/div[1]/div/form");
        form.getInputByName("login").setValueAttribute(login);
        form.getInputByName("pass").setValueAttribute(password);
        form.getInputByName("login_ok").click();


        System.out.println("Выполнен вход");
    }

    public void get() throws IOException
    {
        String requestURL = "https://www.keft.ru/user/" + login;
        HtmlPage htmlPage;

        htmlPage = webClient.getPage(requestURL);
        System.out.println(htmlPage.asXml());


        System.out.println("\n\n^^^^ GET REQUEST ^^^^\n\n");
    }

    public void head() throws IOException
    {
        URL url = new URL("https://www.keft.ru");
        WebRequest requestSettings = new WebRequest(url, HttpMethod.HEAD);
        HtmlPage page = webClient.getPage(requestSettings);
        List<NameValuePair> response = page.getWebResponse().getResponseHeaders();
        for (NameValuePair header : response)
        {
            System.out.println(header.getName() + " = " + header.getValue());
        }

        System.out.println("\n\n^^^^ HEAD REQUEST ^^^^\n\n");
    }

    public void options() throws IOException
    {
        URL url = new URL("https://www.keft.ru/purse/");
        WebRequest requestSettings = new WebRequest(url, HttpMethod.OPTIONS);
        HtmlPage page = webClient.getPage(requestSettings);
        System.out.println(page.asXml());

        System.out.println("\n\n^^^^ OPTIONS REQUEST ^^^^\n\n");
    }

    public void post() throws IOException
    {
        String requestBody = "birth=1990-1-14&firstname=вася&lastname=&biographia=&country=&city=&icq=&webpage=";
        URL requestURL = new URL("https://www.keft.ru/ajax/settings_info.php");
        WebRequest requestSettings = new WebRequest(requestURL, HttpMethod.POST);
        requestSettings.setRequestBody(requestBody);

        webClient.getPage(requestSettings);
        System.out.println("Поле имя профиля обновлено");
        System.out.println("\n\n^^^^ POST REQUEST ^^^^\n\n");
    }

    public void getAllGames() throws IOException
    {
        KeftURLHandler keftURLHandler = new KeftURLHandler();
        GamesDownloader gamesDownloader = new GamesDownloader(webClient);
        String requestURL = "https://www.keft.ru";
        HtmlPage htmlPage;

        htmlPage = webClient.getPage(requestURL);
        ArrayList<String> gamesURLs = keftURLHandler.getGamesURLs(htmlPage);
        gamesDownloader.downloadGamesPages(gamesURLs);
    }

}
