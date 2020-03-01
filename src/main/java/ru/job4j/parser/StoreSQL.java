package ru.job4j.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for save vacancies in database and load from database.
 * @author Viktor
 * @version 1.0
 */
public class StoreSQL implements AutoCloseable {
    private static final Logger LOG = LogManager.getLogger(StoreSQL.class.getName());
    private Connection connection;

    public StoreSQL(Connection connection) {
        this.connection = connection;
    }

    public StoreSQL() {

    }

    public boolean init(Config config) {
        try {
            Class.forName(config.get("driver-class-name"));
            this.connection = DriverManager.getConnection(
                    config.get("url"),
                    config.get("username"),
                    config.get("password")
            );
        } catch (Exception ex) {
            LOG.error("message", ex);
        }
        return connection != null;
    }

    /**
     * Save list vacancies in database.
     * @param vacancies list
     */
    public void save(List<Vacancy> vacancies) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO vacancies (name, text, link) values(?, ?, ?) ON CONFLICT (name) DO NOTHING");
            for (Vacancy vacancy : vacancies) {
                statement.setString(1, vacancy.getName());
                statement.setString(2, vacancy.getText());
                statement.setString(3, vacancy.getLink());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException ex) {
            LOG.error("message", ex);
        }
    }

    /**
     * Load from database list vacancy.
     * @return list vacancies.
     */
    public List<Vacancy> load() {
        List<Vacancy> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM vacancies");
            while (set.next()) {
                Vacancy vacancy = new Vacancy(
                        set.getString("name"),
                        set.getString("text"),
                        set.getString("link")
                );
                result.add(vacancy);
            }
        } catch (SQLException ex) {
            LOG.error("message", ex);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
