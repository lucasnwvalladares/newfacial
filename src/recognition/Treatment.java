/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

/**
 *
 * @author Lucas Nicolás
 */
public class Treatment {
    public static void main(String[] args) {
        OpenCVFrameConverter.ToMat convertMat = new OpenCVFrameConverter.ToMat();
        
        CascadeClassifier faceDetector = new CascadeClassifier("src\\resources\\haarcascade_frontalface_alt.xml");
        
        Frame capturedFrame = null;
        Mat colorfulImg = new Mat();
    }
}
