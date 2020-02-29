package ru.job4j.parser;

import java.util.Objects;

/**
 * Class for vacancy.
 * @author Viktor
 * @version 1.0
 */
public class Vacancy {
    private String name;
    private String text;
    private String link;

    public Vacancy(String name, String text, String link) {
        this.name = name;
        this.text = text;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Vacancy vacancy = (Vacancy) o;
        return name.equals(vacancy.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
