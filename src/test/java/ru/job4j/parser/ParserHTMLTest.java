package ru.job4j.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserHTMLTest {

    @Test
    public void whenParseVacanciesPagesThenListSizeEquals14() {
        Config config = new Config("app.properties");
        config.init();
        config.set("address", "https://vikkingg13.github.io");
        ParserHTML parser = new ParserHTML(
                config.get("address"),
                config.get("regular"),
                config.get("start-date")
        );
        assertEquals(parser.parse().size(), 14);
    }

    @Test
    public void whenParseVacanciesPagesWithLessDateRangeThenReturnListSizeEquals8() {
        Config config = new Config("app.properties");
        config.init();
        config.set("address", "https://vikkingg13.github.io");
        config.set("start-date", "1 фев 20, 00:00");
        ParserHTML parser = new ParserHTML(
                config.get("address"),
                config.get("regular"),
                config.get("start-date")
        );
        assertEquals(parser.parse().size(), 8);
    }

    @Test
    public void whenParseVacanciesPagesThenFirstVacanciesNameEqualsJavaDeveloper() {
        Config config = new Config("app.properties");
        config.init();
        config.set("address", "https://vikkingg13.github.io");
        ParserHTML parser = new ParserHTML(
                config.get("address"),
                config.get("regular"),
                config.get("start-date")
        );
        String expected = "Java разработчик / Backend developer (Spring Boot), до 220, м. Технопарк";
        assertEquals(parser.parse().get(0).getName(), expected);
    }
}
