/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import java.io.File;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.opencv_face.*; 
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

/**
 *
 * @author Lucas Nicolás
 */
public class YaleTest {
    public static void main(String[] args) {
        int totalHits = 0;
        double percentualHit = 0;
        double totalTrust = 0;
        
        //FaceRecognizer reconhecedor = EigenFaceRecognizer.create();
        //FaceRecognizer reconhecedor = FisherFaceRecognizer.create();
        FaceRecognizer recognizer = LBPHFaceRecognizer.create();

        //reconhecedor.read("src\\recursos\\classificadorEigenfacesYale.yml");
        //reconhecedor.read("src\\recursos\\classificadorFisherfacesYale.yml");
        recognizer.read("src\\resources\\classifierLBPHYale.yml");

        File source = new File("src\\yalefaces\\test");
        File[] files = source.listFiles();
        
        for (File image : files) {           
            Mat picture = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);
            int clazz = Integer.parseInt(image.getName().substring(7, 9));          
            resize(picture, picture, new Size(160, 160));

            IntPointer tag = new IntPointer(1);
            DoublePointer trust = new DoublePointer(1);
            recognizer.predict(picture, tag, trust);
            int prediction = tag.get(0);
            System.out.println(clazz + " foi reconhecido como " + prediction + " - " + trust.get(0));
            if (clazz == prediction) {
                totalHits++;
                totalTrust += trust.get(0);
            }
        }
        
        percentualHit = (totalHits / 30.0) * 100;
        totalTrust = totalTrust / totalHits;
        System.out.println("Percentual de acerto: " + percentualHit);
        System.out.println("Total confiança: " + totalTrust);
    }
}
