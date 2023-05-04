package com.example.aton_final_project.util.parse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parsing {
    public static String parsingFileName(String username) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        return username + "_" +dateFormatter.format(new Date());
    }
}
