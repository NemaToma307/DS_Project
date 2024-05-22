package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MonitoringService extends Remote {
    byte[] captureImage() throws RemoteException;
    byte[] captureScreen() throws RemoteException;
}
