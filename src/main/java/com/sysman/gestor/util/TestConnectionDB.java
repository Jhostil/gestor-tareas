package com.sysman.gestor.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnectionDB {

    public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
        String usuario = "TASKS_USER";
        String contrasena = "password";

        try {
            Connection conn = DriverManager.getConnection(url, usuario, contrasena);

            if (conn != null) {
                System.out.println("Conexión exitosa");
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
