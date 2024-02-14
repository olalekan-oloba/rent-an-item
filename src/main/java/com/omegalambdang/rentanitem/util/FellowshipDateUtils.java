package com.omegalambdang.rentanitem.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class FellowshipDateUtils {

    public static final String DATE_INPUT_FORMAT ="dd/MM/yyyy";
    public static final String DATE_TIME_INPUT_FORMAT ="dd/MM/yyyy hh:mm";
    public static final String DATE_DISPLAY_FORMAT="MMM dd,yyyy";
    public static final String DATE_TIME_DISPLAY_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String DATE_DB_FORMAT ="yyyy-MM-dd" ;
    public static final String DATE_TIME_DB_FORMAT ="yyyy-MM-dd'T'HH:mm:ssZ" ;

    public static boolean isValidFormat(String format, String value) {
        if(value==null){
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
            return date != null;
        }catch (Exception e){
            log.error("unable to validate date format",e);
        }
        return false;
    }



    public static String getPresentYear() {
        Date dt = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(dt.getTime()));
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate lastDateOfMonth(short year, short month) {
        return YearMonth.of( year , month ).atEndOfMonth();
    }

    public static LocalDate firstDateOfMonth(short year, short month) {
        return YearMonth.of( year , month ).atDay(1);
    }


    /*
     return integer value of current month valid range 1-12
     */
    public static short getPresentMonthValue() {
        return  (short)YearMonth.from(Instant.now().atZone(ZoneId.of("UTC"))).getMonthValue();
    }

    /*
     return integer value of current month valid range 1-12
     */
    public static short getPresentYearValue() {
        return  (short)YearMonth.from(Instant.now().atZone(ZoneId.of("UTC"))).getYear();
    }

    public static LocalDate lastDateOfYear(short year) {
        return YearMonth.of( year , 12 ).atEndOfMonth();
    }

    public static LocalDate firstDateOfYear(short year) {
        return firstDateOfMonth(year,(short)1);
    }

    public static String formatLocalDateToString(LocalDate localDate, String pattern) {
        Objects.requireNonNull(localDate);
        Objects.requireNonNull(pattern);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return localDate.format(formatter);
        } catch (DateTimeException e) {
            log.error("Cannot format date to string",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * formate string date to Local date
     * @param dateString string to be formatted
     * @param pattern the pattern of string to be formatted e.g to format e.g if "12/03/2005" then pattern dd/MM/yyyy
     * @return
     */
    public static LocalDate formatStringToLocalDate(String dateString, String pattern) {
        Objects.requireNonNull(dateString);
        Objects.requireNonNull(pattern);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeException e) {
            log.error("Cannot string to date",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * using default pattern "yyyy-MM-dd"
     * @param dateString
     * @return
     */
    public static LocalDate formatStringToLocalDate(String dateString) {
        Objects.requireNonNull(dateString);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeException e) {
            log.error("Cannot string to date",e);
            throw new RuntimeException(e);
        }
    }

    public static Instant now() {
       // return LocalDateTime.now(ZoneId.systemDefault());
        return Instant.now();
    }

    public static Date formatStringToDate(String dateString) {
        Objects.requireNonNull(dateString);
        try {
            SimpleDateFormat sdFormatter = new SimpleDateFormat(
                    DATE_DB_FORMAT);
            return sdFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


}
