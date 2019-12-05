/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import java.awt.event.KeyEvent;
import java.util.Scanner;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_core.*;

/**
 *
 * @author Lucas Nicolás
 */
public class Capture {
    public static void main(String arg[]) throws FrameGrabber.Exception, InterruptedException {
        KeyEvent tecla = null;
        OpenCVFrameConverter.ToMat convertMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
        camera.start();
        
        CascadeClassifier faceDetector = new CascadeClassifier("src\\resources\\haarcascade_frontalface_alt.xml");
        
        CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
        Frame capturedFrame = null;
        Mat colorfulImg = new Mat();
        int samplesNumber = 5;
        int sample = 1;
        System.out.println("Digite seu id: ");
        Scanner register = new Scanner(System.in);
        int personID = register.nextInt();
        while ((capturedFrame = camera.grab()) != null) {
            colorfulImg = convertMat.convert(capturedFrame);
            Mat grayImg = new Mat();
            System.out.println(grayImg); 
            System.out.println(colorfulImg); 
            cvtColor(colorfulImg, grayImg, COLOR_BGRA2GRAY);
            RectVector detectedFaces = new RectVector();
            faceDetector.detectMultiScale(grayImg, detectedFaces, 1.1, 1, 0, new Size(150,150), new Size(500,500));
            if (tecla == null) {
                tecla = cFrame.waitKey(5);
            }
            for (int i=0; i < detectedFaces.size(); i++) {
                Rect faceData = detectedFaces.get(0);
                rectangle(colorfulImg, faceData, new Scalar(0,0,255, 0));
                Mat capturedFace = new Mat(grayImg, faceData);
                resize(capturedFace, capturedFace, new Size(160,160));
                if (tecla == null) {
                    tecla = cFrame.waitKey(5);
                }
                if (tecla != null) {
                    if (tecla.getKeyChar() == 'q') {
                        if (sample <= samplesNumber) {
                            imwrite("src\\pictures\\person." + personID + "." + sample + ".jpg", capturedFace);
                            System.out.println("Foto " + sample + " capturada\n");
                            sample++;
                        }
                    }
                    tecla = null;
                }
            }
            if (tecla == null) {
                tecla = cFrame.waitKey(20);
            }
            if (cFrame.isVisible()) {
                cFrame.showImage(capturedFrame);
            }
            
            if (sample > samplesNumber) {
                break;
            }
        }
        cFrame.dispose();
        camera.stop();
    }
}
