package com.planbuyandeat.SQLite.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDate {
    private long id;
    private Date component;
    private SimpleDateFormat format;
    private DateFormat dayOfMonthParser;
    private DateFormat monthParser;
    private DateFormat yearParser;

    public CustomDate() {
        component = new Date();
        format = new SimpleDateFormat("dd-MM-yyyy");
        dayOfMonthParser = new SimpleDateFormat("dd");
        monthParser  = new SimpleDateFormat("MM");
        yearParser = new SimpleDateFormat("yyyy");
    }


    /** Getters et Setters **/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGetDayOfMonth(String day){
        try {
            component.setTime(format.parse(
                    day + "-" + getMonth() + "-" + getYear()
            ).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getTime(){
        return component.getTime();
    }

    public void setTime(long time){
        this.component.setTime(time);
    }

    public Date getDate() {
        return component;
    }

    public void setDate(Date component) {
        this.component = component;
    }

    public void setDate(String date) {
        try {
            this.component = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDayOfMonth(){
        return dayOfMonthParser.format(component);
    }

    public void setMonth(String month){
        try {
            component.setTime(format.parse(
                    getDayOfMonth() + "-" + month + "-" + getYear()
            ).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getMonth(){
        return String.valueOf(Integer.parseInt(monthParser.format(component)));
    }

    public void setYear(String year){
        try {
            component.setTime(format.parse(
                    getDayOfMonth() + "-" + getMonth() + "-" + year
            ).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getYear(){
        return yearParser.format(component);
    }


    /**
     * Increment/decrement une date donnée
     * @param days le nombre de jours à ajouter/retirer (si negatif)
     */
    public void addDays(int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(component);
        cal.add(Calendar.DATE, days);
        component.setTime(cal.getTime().getTime());
    }

    public String toString(){
        return format.format(component);
    }
}
