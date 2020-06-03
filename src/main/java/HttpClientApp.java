import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

public class HttpClientApp
{
    public static void main(String[] args)
    {
        KeftParser keftParser = new KeftParser(new WebClient());
        try
        {
            keftParser.auth();
            keftParser.get();
            keftParser.post();
            keftParser.head();
            keftParser.options();
            keftParser.getAllGames();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
