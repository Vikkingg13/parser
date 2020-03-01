package ru.job4j.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility for convert Date to string and reverse by format.
 * @author Viktor
 * @version 1.0
 */
public class DateUtil {
    private static final Logger LOG = LogManager.getLogger(DateUtil.class.getName());
    private static DateFormatSymbols formatSymbols = new DateFormatSymbols() {
        @Override
        public String[] getMonths() {
            return new String[] {
                    "янв", "фев", "мар", "апр", "май", "июл", "июн", "авг", "сен", "окт", "ноя", "дек"
            };
        }
    };
    private static SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yy, HH:mm", formatSymbols);

    /**
     * Convert String to date.
     * @param date
     * @return Date
     */
    public static Date toDate(String date) {
        Date result = null;
        String[] tokens = date.split(",");
        Calendar token = Calendar.getInstance();
        switch (tokens[0]) {
            case "сегодня":
                date = String.format(
                        "%s,%s", formatter.format(token.getTime()).split(",")[0], tokens[1]);
                break;
            case "вчера":
                token.add(Calendar.DAY_OF_YEAR, -1);
                date = String.format(
                        "%s,%s", formatter.format(token.getTime()).split(",")[0], tokens[1]);
                break;
            default:
                break;
        }
        try {
            result = formatter.parse(date);
        } catch (ParseException ex) {
            LOG.error("message", ex);
        }
        return result;
    }

    /**
     * Convert Date to String.
     * @param date
     * @return string format date.
     */
    public static String format(Date date) {
        return formatter.format(date);
    }
}
