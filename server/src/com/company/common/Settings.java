package com.company.common;

import java.util.ResourceBundle;

public class Settings {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("settings");

    public static final String HOST = bundle.getString("host");;
    public static final int PORT = Integer.parseInt(bundle.getString("port"));;


}
