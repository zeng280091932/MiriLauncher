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
public class WeatherDocument implements Serializable {

    private static final long serialVersionUID = -7159791369991998243L;

    @SerializedName("imgoweatherinfo")
    private WeatherInfo weatherInfo;

    public WeatherDocument() {
        super();
    }

    public WeatherDocument(WeatherInfo weatherInfo) {
        super();
        this.weatherInfo = weatherInfo;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    @Override
    public String toString() {
        return "WeatherDocument [weatherInfo=" + weatherInfo + "]";
    }

}
