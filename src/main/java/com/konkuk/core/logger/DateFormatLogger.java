package com.konkuk.core.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatLogger implements SystemLogger {

    private final DateFormat dateFormat;

    public DateFormatLogger() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    @Override
    public void info(String msg) {
        System.out.println(new StringBuilder()
                .append("[INFO]:")
                .append("[")
                .append(getTime())
                .append("]")
                .append(":")
                .append(msg)
                );
    }

    @Override
    public void error(String msg) {

    }

    private String getTime() {
        return dateFormat.format(new Date());
    }
}
