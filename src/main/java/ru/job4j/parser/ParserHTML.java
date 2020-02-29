package ru.job4j.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser vacancies.
 * @author Viktor
 * @version 1.0
 */
public class ParserHTML {
    private static final Logger LOG = LogManager.getLogger(ParserHTML.class.getName());
    private String address;
    private Pattern pattern;
    private Date startDate;
    private int pageNumber = 1;

    public ParserHTML(String address, String regular, String startDate) {
        this.address = address;
        this.pattern = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
        this.startDate = DateUtil.toDate(startDate);
    }

    /**
     * Parse site with vacancies.
     * @return List with found vacancies.
     */
    public List<Vacancy> parse() {
        List<Vacancy> result = new ArrayList<>();
        Date date;
        try {
            Elements elements;
            do {
                elements = nextPage();
                for (Element element : elements) {
                    if (isActual(element)) {
                        vacancy(element).ifPresent(result::add);
                    }
                }
                date = DateUtil.toDate(elements.last().select("td").last().text());
            } while (date.after(startDate));
            LOG.info("Found vacancies: {}", result.size());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    /**
     * Load vacancies next page.
     * @return Elements vacancy
     * @throws IOException
     */
    private Elements nextPage() throws IOException {
        Document document = Jsoup.connect(address).get();
        Elements elements = document.select(".forumTable tr");
        elements.remove(0);
        pageNumber++;
        String query = String.format(".sort_options a:contains(%s)", pageNumber);
        address = document.select(query).get(0).attr("href");
        return elements;
    }

    /**
     * Checks for actual vacancy.
     * @param element
     * @return True if vacancy actual else false.
     */
    private boolean isActual(Element element) {
        String name = element.select("a").get(0).text();
        String date = element.select("td").get(5).text();
        Matcher matcher = pattern.matcher(name);
        return matcher.matches() && DateUtil.toDate(date).after(startDate);
    }

    /**
     * Create vacancy object.
     * @param element
     * @return Optional<Vacancy>
     * @throws IOException
     */
    private Optional<Vacancy> vacancy(Element element) throws IOException {
        Optional<Vacancy> vacancy = Optional.empty();
        String name = element.select("a").get(0).text();
        String link = element.select("a").attr("href");
        Document doc = Jsoup.connect(link).get();
        Element table = doc.select(".msgTable").get(0);
        String text = table.select(".msgBody").get(1).text();
        Date date = DateUtil.toDate(table.select(".msgFooter").get(0).text());
        if (date.after(startDate)) {
            vacancy = Optional.of(new Vacancy(name, text, link));
            LOG.info("Name:{}", name);
            LOG.info("Date:{}", DateUtil.format(date));
            LOG.info("Link:{}", link);
        }
        return vacancy;
    }
}
