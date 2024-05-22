package main;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MonitoringServiceImpl extends UnicastRemoteObject implements MonitoringService {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    protected MonitoringServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public byte[] captureImage() throws RemoteException {
        System.out.println("Capturing image...");
        VideoCapture camera = new VideoCapture(0, Videoio.CAP_DSHOW);
        if (!camera.isOpened()) {
            throw new RemoteException("Error: Camera not accessible");
        }

        Mat frame = new Mat();
        camera.read(frame);

        BufferedImage image = matToBufferedImage(frame);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.release();
        System.out.println("Image captured successfully.");
        return baos.toByteArray();
    }

    @Override
    public byte[] captureScreen() throws RemoteException {
        System.out.println("Capturing screen...");
        BufferedImage screenCapture = null;
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            screenCapture = robot.createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenCapture, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Screen captured successfully.");
        return baos.toByteArray();
    }

    private BufferedImage matToBufferedImage(Mat matrix) {
        int width = matrix.width(), height = matrix.height(), channels = matrix.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        matrix.get(0, 0, sourcePixels);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        return image;
    }
}
