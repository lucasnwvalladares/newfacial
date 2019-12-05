/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import java.io.File;
import java.nio.IntBuffer;

import static org.bytedeco.opencv.global.opencv_core.*; 
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

/**
 *
 * @author Lucas Nicolás
 */
public class YaleTraining {
    public static void main(String[] args) {
        File source = new File("src\\yalefaces\\treining");
        File[] files = source.listFiles();
        MatVector pictures = new MatVector(files.length);
        Mat tags = new Mat(files.length, 1, CV_32SC1);
        IntBuffer tagsBuffer = tags.createBuffer();
        int counter = 0;
      
        for (File image : files) {
            Mat picture = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);
            int clazz = Integer.parseInt(image.getName().substring(7,9));
            resize(picture, picture, new Size(160, 160));
            pictures.put(counter, picture);
            tagsBuffer.put(counter, clazz);
            counter++;
        }
                                                                                 // MUDANÇAS
        FaceRecognizer eigenface = EigenFaceRecognizer.create(30, 0);            // antes: createEigenFaceRecognizer();
        FaceRecognizer fisherface = FisherFaceRecognizer.create(30, 0);          // antes: createFisherFaceRecognizer();
        FaceRecognizer lbph = LBPHFaceRecognizer.create(12, 10, 15, 15, 0);      // antes: createLBPHFaceRecognizer();

        eigenface.train(pictures, tags);
        eigenface.save("src\\resources\\classifierEigenfacesYale.yml");
        
        fisherface.train(pictures, tags);
        fisherface.save("src\\resources\\classifierFisherfacesYale.yml");
        
        lbph.train(pictures, tags);
        lbph.save("src\\resources\\classifierLBPHYale.yml");
    }
}
