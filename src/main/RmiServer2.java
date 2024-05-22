package main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Timer;
import java.util.TimerTask;

public class RmiServer2 {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            MonitoringService service = new MonitoringServiceImpl();

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Naming.rebind("rmi://localhost:1099/MonitoringService", service);
                        System.out.println("Service re-bound to RMI registry");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 60000); // إعادة النشر كل 60 ثانية

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
