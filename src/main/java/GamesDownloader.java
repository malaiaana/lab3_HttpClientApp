import com.gargoylesoftware.htmlunit.WebClient;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GamesDownloader
{
    private final WebClient webClient;
    private ArrayList<String> gamesURLs;
    private final Semaphore maxDownloaders;
    private final Semaphore URLSemaphore;

    public GamesDownloader(WebClient webClient)
    {
        this.webClient = webClient;
        gamesURLs = new ArrayList<String>();
        maxDownloaders = new Semaphore(3);
        URLSemaphore = new Semaphore(1);
    }

    public void downloadGamesPages(ArrayList<String> gamesURLs)
    {
        this.gamesURLs = gamesURLs;
        ArrayList<Thread> downloaders = new ArrayList<Thread>();

        for (int i = 0; i < 4; i++){
            downloaders.add(new Thread(new GameDownloader(this)));
            downloaders.get(i).start();
            try
            {
                downloaders.get(i).join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public WebClient getWebClient()
    {
        return webClient;
    }

    public boolean hasNextGameURL()
    {
        return gamesURLs.size() > 0;
    }

    public String getNextGameURL()
    {
        if (gamesURLs.size() == 0)
        {
            return null;
        }
        String url = gamesURLs.get(0);
        gamesURLs.remove(0);

        return url;
    }

    public Semaphore getMaxDownloadersSemaphore()
    {
        return maxDownloaders;
    }

    public Semaphore getURLSemaphore()
    {
        return URLSemaphore;
    }
}
