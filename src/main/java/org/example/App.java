package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        parsePosts().forEach(System.out::println);

    }

    private static List<Post> parsePosts() throws IOException {
        List<Post> posts = new ArrayList<>();

        Document doc = Jsoup.connect("https://4pda.to").get();
        System.out.println("Идет подключение к главной странице...");
        Elements postTitleElements = doc.getElementsByAttributeValue("itemprop", "url");

        for (Element postTitleElement : postTitleElements) {
            String detailsLink = postTitleElement.attr("href");

            Post post = new Post();
            post.setDetailsLink(detailsLink);
            post.setTitle(postTitleElement.attr("title"));

            System.out.println("Идет подключение к делалям о посте ..." + detailsLink);

            Document postDetailsDoc = Jsoup.connect(detailsLink).get();

            try {
                Element authorElement = postDetailsDoc.getElementsByClass("name")
                        .first()
                        .child(0);

                post.setAuthor(authorElement.text());
                post.setAuthorDetailsLink(authorElement.attr("href"));

            } catch (NullPointerException e) {
                System.out.println("Автор не определен");
                System.out.println("Ссылка не найдена");
                System.out.println("Дата создания не найдена");
            }

            post.setDataOfCreated(postDetailsDoc.getElementsByClass("date")
                    .first()
                    .text());
            posts.add(post);
        }
        return posts;
    }
}
