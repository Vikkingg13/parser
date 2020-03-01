package ru.job4j.parser;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.Date;

public interface Site {
    Elements nextPage();

    String getName(Element element);

    Date getDate(Element element);

    String getLink(Element element);

    Date getCreatedDate(Element element);

    String getText(Element element);

    Date getLastDate(Elements elements);

}
