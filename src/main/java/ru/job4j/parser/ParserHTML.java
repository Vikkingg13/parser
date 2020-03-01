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
    private Site site;
    private Pattern pattern;
    private Date date;

    public ParserHTML(Site site, String regex, String date) {
        this.site = site;
        this.date = DateUtil.toDate(date);
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Parse site with vacancies.
     * @return List with found vacancies.
     */
    public List<Vacancy> parse() {
        List<Vacancy> result = new ArrayList<>();
        Elements elements;
            do {
                elements = site.nextPage();
                for (Element element : elements) {
                    String name = site.getName(element);
                    String link = site.getLink(element);
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.matches()
                            && site.getDate(element).after(date)
                            && site.getCreatedDate(element).after(date)) {
                        String text = site.getText(element);
                        Vacancy vacancy = new Vacancy(name, text, link);
                        result.add(vacancy);
                        LOG.info("Name:{}", name);
                        LOG.info("Link:{}", link);
                    }
                }
            } while (site.getLastDate(elements).after(date));
            LOG.info("Found vacancies: {}", result.size());
        return result;
    }
}
