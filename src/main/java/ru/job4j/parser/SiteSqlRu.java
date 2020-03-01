package ru.job4j.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class SiteSqlRu implements Site {
    private static final Logger LOG = LogManager.getLogger(Quartz.class.getName());
    private String address = "https://www.sql.ru/forum/job-offers";
    private int pageNumber = 1;

    public SiteSqlRu() {
    }

    public SiteSqlRu(String address) {
        this.address = address;
    }

    /**
     * Load vacancies next page.
     * @return Elements vacancy
     * @throws IOException
     */
    public Elements nextPage() {
            Elements elements = null;
            try {
                Document document;
                document = Jsoup.connect(address).get();
                elements = document.select(".forumTable tr");
                elements.remove(0);
                pageNumber++;
                String query = String.format(".sort_options a:contains(%s)", pageNumber);
                address = document.select(query).get(0).attr("href");
            } catch (IOException ex) {
                LOG.error("message", ex);
            }
            return elements;
    }

    /**
     * Checks for actual vacancy.
     * @param element
     * @return True if vacancy actual else false.
     */

    public String getName(Element element) {
        return element.select("a").get(0).text();
    }

    public Date getDate(Element element) {
        String date = element.select("td").get(5).text();
        return DateUtil.toDate(date);
    }

    public String getLink(Element element) {
        return element.select("a").attr("href");
    }

    public Date getCreatedDate(Element element) {
        Element table = null;
        try {
            Document doc = Jsoup.connect(getLink(element)).get();
            table = doc.select(".msgTable").get(0);
        } catch (IOException ex) {
            LOG.error("message", ex);
        }
        return DateUtil.toDate(table.select(".msgFooter").get(0).text());
    }

    public String getText(Element element) {
        Element table = null;
        try {
            Document doc = Jsoup.connect(getLink(element)).get();
            table = doc.select(".msgTable").get(0);
        } catch (IOException ex) {
            LOG.error("message", ex);
        }
        return table.select(".msgBody").get(1).text();
    }

    public Date getLastDate(Elements elements) {
        return DateUtil.toDate(elements.last().select("td").last().text());
    }

    /**
     * Create vacancy object.
     * @param element
     * @return Optional<Vacancy>
     * @throws IOException
     */
}
