/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import org.bytedeco.opencv.opencv_face.*;

/**
 *
 * @author Lucas Nicolás
 */
public class JavaCVTest {
    public static void main(String[] args) {
        FaceRecognizer r = LBPHFaceRecognizer.create();
    }
}
