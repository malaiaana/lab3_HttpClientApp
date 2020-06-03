import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class GameDownloader implements Runnable
{
    private final GamesDownloader gamesDownloader;
    private final WebClient webClient;

    public GameDownloader(GamesDownloader gamesDownloader)
    {
        this.gamesDownloader = gamesDownloader;
        webClient = new WebClient();
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setCookieManager(gamesDownloader.getWebClient().getCookieManager());
    }

    private String downloadGame(String url)
    {
        HtmlPage page;
        try
        {
            page = webClient.getPage(url);
            return page.asXml();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public void run()
    {
        try
        {
            gamesDownloader.getMaxDownloadersSemaphore().acquire();

            while (gamesDownloader.hasNextGameURL())
            {
                gamesDownloader.getURLSemaphore().acquire();
                String url = gamesDownloader.getNextGameURL();
                gamesDownloader.getURLSemaphore().release();

                System.out.println(downloadGame(url));
                System.out.println("\n\n GAME DOWNLOADED\n\n");
            }

            gamesDownloader.getMaxDownloadersSemaphore().release();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
