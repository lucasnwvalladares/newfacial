/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_imgproc.FONT_HERSHEY_PLAIN;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*; 

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.putText;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 *
 * @author Lucas Nicolás
 */
public class Recognition {
    public static void main(String args[]) throws FrameGrabber.Exception {
        OpenCVFrameConverter.ToMat convertMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
        String[] people = {"", "Lucas", "Natali"};
        camera.start();
        
        CascadeClassifier faceDetector = new CascadeClassifier("src\\resources\\haarcascade_frontalface_alt.xml");
        
        //FaceRecognizer reconhecedor = EigenFaceRecognizer.create();             // *antes: createEigenFaceRecognizer();;;
        //reconhecedor.read("src\\recursos\\classificadorEigenFaces.yml");        // *antes: load()
        //reconhecedor.setThreshold(0);
        
        //FaceRecognizer reconhecedor = FisherFaceRecognizer.create();
        //reconhecedor.read("src\\recursos\\classificadorFisherFaces.yml");
        
        FaceRecognizer recognizer = LBPHFaceRecognizer.create();
        recognizer.read("src\\resources\\classifierLBPH.yml");
        
        
        CanvasFrame cFrame = new CanvasFrame("Reconhecimento", CanvasFrame.getDefaultGamma() / camera.getGamma());
        Frame capturedFrame = null;
        Mat colorfulImg = new Mat();
        
        while ((capturedFrame = camera.grab()) != null) {
            colorfulImg = convertMat.convert(capturedFrame);
            Mat grayImg = new Mat();
            cvtColor(colorfulImg, grayImg, COLOR_BGRA2GRAY);
            RectVector detectedFaces = new RectVector();
            faceDetector.detectMultiScale(grayImg, detectedFaces, 1.1, 2, 0, new Size(100,100), new Size(500,500));
            for (int i = 0; i < detectedFaces.size(); i++) {
                Rect faceData = detectedFaces.get(i);
                rectangle(colorfulImg, faceData, new Scalar(0,255,0,0));
                Mat capturedFace = new Mat(grayImg, faceData);
                
                IntPointer tag = new IntPointer(1);
                DoublePointer trust = new DoublePointer(1);
                
                System.out.println("w="+capturedFace.size(0)+"  /  h="+capturedFace.size(1));
                if ((capturedFace.size(0) == 160) || (capturedFace.size(1) == 160)){
                    continue;
                }  
                resize(capturedFace, capturedFace, new Size(160,160));
                //Size tamanho = new Size(faceCapturada); 
                recognizer.predict(capturedFace, tag, trust);
                int prediction = tag.get(0);
                String name;
                if (prediction == -1) {
                    name = "Desconhecido";
                } else {
                    name = people[prediction] + " - " + trust.get(0);
                }
                
                int x = Math.max(faceData.tl().x() - 10, 0);
                int y = Math.max(faceData.tl().y() - 10, 0);
                putText(colorfulImg, name, new Point(x, y), FONT_HERSHEY_PLAIN, 1.4, new Scalar(0,255,0,0));
            }
            if (cFrame.isVisible()) {
                cFrame.showImage(capturedFrame);
            }
        }
        cFrame.dispose();
        camera.stop();
    }
}
