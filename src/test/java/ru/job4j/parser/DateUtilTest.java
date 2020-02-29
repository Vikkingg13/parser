package ru.job4j.parser;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {
    @Test
    public void whenDateToFormatThenReturnStringDateFormat() {
        Calendar calendar =
                new GregorianCalendar(2020, Calendar.JANUARY, 1);
        String expected = "1 янв 20, 00:00";
        assertEquals(DateUtil.format(calendar.getTime()), expected);
    }

    @Test
    public void whenStringToDateThenReturnDate() {
        String string = "1 янв 20, 00:00";
        Date expected = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
        assertEquals(DateUtil.toDate(string), expected);
    }
}
