package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Crawler {

    private HashSet<String> urlLinks;
    int MAX_Depth = 2;
    public Connection connection;

    public Crawler(){
        connection = DataBaseConnection.getConnection();
        urlLinks = new HashSet<>();

    }
    public void getPageTextAndLinks(String url, int depth){
        if(!urlLinks.contains(url)) {
            if (urlLinks.add(url))
                System.out.println(url);

            try {
                // parsing Html object to java Document Object
                Document document = Jsoup.connect(url).timeout(5000).get();
                String text = document.text().length() < 501? document.text() : document.text().substring(0, 500);

                PreparedStatement preparedStatement = connection.prepareStatement("insert into pages values(?,?,?)");
                preparedStatement.setString(1, document.title());
                preparedStatement.setString(2,url);
                preparedStatement.setString(3,text);
                preparedStatement.executeUpdate();

                depth++;

                if (depth > MAX_Depth)
                    return;

                Elements availableLinksOnPage = document.select("a[href]");
                for(Element currentLink: availableLinksOnPage){
                    getPageTextAndLinks(currentLink.attr("abs:href"), depth);
                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Crawler crawler = new Crawler();
        crawler.getPageTextAndLinks("https://javatpoint.com/",0);

    }
}