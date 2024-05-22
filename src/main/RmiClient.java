package main;

import java.rmi.Naming;

public class RmiClient {
    public static void main(String[] args) {
        try {
            main.MonitoringService service = (main.MonitoringService) Naming.lookup("rmi://localhost:1099/MonitoringService");

            // التقاط صورة
            byte[] imageBytes = service.captureImage();
            System.out.println("Image captured: " + imageBytes.length + " bytes");

            // التقاط لقطة شاشة
            byte[] screenBytes = service.captureScreen();
            System.out.println("Screen captured: " + screenBytes.length + " bytes");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
