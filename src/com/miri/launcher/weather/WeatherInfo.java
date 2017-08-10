/* 
 * 文件名：WeatherInfo.java
 * 版权：Copyright
 */
package com.miri.launcher.weather;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class WeatherInfo implements Serializable {

    private static final long serialVersionUID = 1326903646944806566L;

    @SerializedName("city")
    private String city;

    @SerializedName("date_y")
    private String date;

    @SerializedName("week")
    private String week;

    @SerializedName("temp1")
    private String temp;

    @SerializedName("wind1")
    private String wind;

    @SerializedName("weather1")
    private String weather;

    @SerializedName("img1")
    private String img;

    public WeatherInfo() {

    }

    public WeatherInfo(String city, String date, String week, String temp, String wind,
            String weather, String img) {
        super();
        this.city = city;
        this.date = date;
        this.week = week;
        this.temp = temp;
        this.wind = wind;
        this.weather = weather;
        this.img = img;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "WeatherInfo [city=" + city + ", date=" + date + ", week=" + week + ", temp=" + temp
                + ", wind=" + wind + ", weather=" + weather + ", img=" + img + "]";
    }

}
