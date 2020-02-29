package ru.job4j.parser;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StoreSQLTest {
    public Connection init() throws Exception {
            Config config = new Config("app.properties");
            config.init();
            Class.forName(config.get("driver-class-name"));
            return DriverManager.getConnection(
                    config.get("url"),
                    config.get("username"),
                    config.get("password")
            );
    }

    @Test
    public void whenConnectionSuccessThenReturnTrue() throws Exception {
        try (StoreSQL store = new StoreSQL()) {
            Config config = new Config("app.properties");
            config.init();
            assertTrue(store.init(config));
        }
    }

    @Test
    public void whenSaveDuplicateVacanciesThenSaveOnlyUniqueVacancy() throws Exception {
        try (StoreSQL store = new StoreSQL(ConnectionRollback.create(init()))) {
            store.save(List.of(
                    new Vacancy("Vacancy", "Vacancy first", "www.sql.ru"),
                    new Vacancy("Vacancy", "Vacancy second", "www.sql.ru/page2"))
            );
            List<Vacancy> vacancies = store.load();
            assertEquals(vacancies.size(), 1);
            assertEquals(vacancies.get(0).getText(), "Vacancy first");
        }
    }

    @Test
    public void whenSaveVacanciesListThenDataBaseHasVacancies() throws Exception {
        try (StoreSQL store = new StoreSQL(ConnectionRollback.create(init()))) {
            store.save(List.of(
                    new Vacancy("Vacancy", "Vacancy first", "www.sql.ru"),
                    new Vacancy("Vacancy second", "Vacancy second", "www.sql.ru/page2"))
            );
            List<Vacancy> vacancies = store.load();
            assertEquals(vacancies.size(), 2);
            assertEquals(vacancies.get(0).getName(), "Vacancy");
        }
    }
}
