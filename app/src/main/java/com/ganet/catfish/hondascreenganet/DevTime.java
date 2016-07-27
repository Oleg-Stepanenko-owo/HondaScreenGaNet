package com.ganet.catfish.hondascreenganet;

/**
 * Created by oleg on 26.07.2016.
 */
public class DevTime {
    private String hour;
    private String min;

    DevTime( String data ) {
        hour = "00";
        min = "00";

        hour = data.substring( 0, 2);
        min = data.substring( 2, 4);
        hour.replace("F", "");
    }

    public String getTime() {
        return (hour + ":" + min);
    }
}
