import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeftURLHandler
{

    public ArrayList<String> getGamesURLs(HtmlPage mainPage)
    {
        HtmlDivision div = (HtmlDivision) mainPage.getElementById("MenuGames");

        String regex = "<a.*http.*.ru";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(div.asXml());
        ArrayList<String> tmp = new ArrayList<String>();
        ArrayList<String> gamesURLs = new ArrayList<String>();

        while (matcher.find())
        {
            tmp.add(matcher.group(0));
        }

        regex = "http.*";
        pattern = Pattern.compile(regex, Pattern.MULTILINE);
        for (String a : tmp)
        {
            matcher = pattern.matcher(a);
            if (matcher.find())
            {
                gamesURLs.add(matcher.group(0));
            }
        }

        return gamesURLs;
    }
}
