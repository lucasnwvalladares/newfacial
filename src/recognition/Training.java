/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;


/**
 *
 * @author Lucas Nicolás
 */
public class Training {
    public static void main(String args[]) {
        File source = new File("src\\pictures");
        FilenameFilter imgFilter = new FilenameFilter() {
            @Override
            public boolean accept(File src, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
            }
        };
        
        File[] files = source.listFiles(imgFilter);
        MatVector pictures = new MatVector(files.length);
        Mat tags = new Mat(files.length, 1, CV_32SC1);
        IntBuffer tagsBuffer = tags.createBuffer();
        int counter = 0;
        for (File image: files) {
            Mat picture = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);
            int classe = Integer.parseInt(image.getName().split("\\.")[1]);
            System.out.println(image.getName().split("\\.")[1] + "  " + image.getAbsolutePath());
            Mat grayImg = new Mat();
            //cvtColor(picture, grayImg, COLOR_BGRA2GRAY);
            resize(picture, grayImg, new Size(160,160));
            pictures.put(counter, grayImg);
            tagsBuffer.put(counter, classe);
            counter++;
        }

        FaceRecognizer eigenfaces = EigenFaceRecognizer.create(); 
        FaceRecognizer fisherfaces = FisherFaceRecognizer.create(); 
        FaceRecognizer lbph = LBPHFaceRecognizer.create(2,9,9,9,1);
        
        eigenfaces.train(pictures, tags);
        eigenfaces.save("src\\resources\\classifierEigenFaces.yml");
        fisherfaces.train(pictures, tags);
        fisherfaces.save("src\\resources\\classifierFisherFaces.yml");
        lbph.train(pictures, tags);
        lbph.save("src\\resources\\classifierLBPH.yml");
    }
}
