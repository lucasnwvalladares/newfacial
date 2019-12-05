/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognition;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_core.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.equalizeHist;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

/**
 *
 * @author Lucas Nicolás
 */
public class FaceCaptureByImage {
 
    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception, InterruptedException{
        
        KeyEvent tecla = null;
        
        OpenCVFrameConverter.ToMat converterMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToIplImage converterFrame = new OpenCVFrameConverter.ToIplImage();
        
        // haarcascade para deteccao de face
        CascadeClassifier face_cascade = new CascadeClassifier("src\\resources\\haarcascade_frontalface_alt.xml");
 
        // Componente para criar o preview da imagem
        CanvasFrame cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma());
 
        Frame capturedFrame = null;             // armazena a imagem do frame atual capturado
        Mat colorfulImgMat = new Mat();       // imagem convertido para mat
        
        int totalSamples = 5;      // numero total de fotos que vao ser tiradas
        int samples = 1;              // para contar quantas fotos ja foram tiradas (amostra atual)
 
        
        System.out.println("Digite seu ID: "); 
        Scanner register = new Scanner(System.in);
        int personID = register.nextInt();            // id da pessoa que vai ser cadastrada

        File diretorio = new File("src\\newpictures"); // diretorio onde estão as fotos 
        File[] arquivos = diretorio.listFiles();
        MatVector fotos = new MatVector(arquivos.length);
        Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
        IntBuffer rotulosBuffer = rotulos.createBuffer();
        int cont = 0;
        
        for (File imagem : arquivos) {
            System.out.println(imagem);
            colorfulImgMat = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
            fotos.put(colorfulImgMat);
            System.out.println(imagem.getAbsolutePath());
            
            //IplImage image = cvLoadImage(imagem.getAbsolutePath(), 1);
            //Mat mtx(image);
            
            //converte para tons de cinza
            //cvtColor(imgMat, imgMatCinza, COLOR_BGRA2GRAY);
            //equaliza o histograma - normaliza a imagem aumentando o contraste
            //equalizeHist(imgMatCinza, imgMatCinza);
            
            RectVector faces = new RectVector();
            
            //encontra as faces
            //face_cascade.detectMultiScale(colorfulImgMat, faces,
            //        1.1, //scale factor
            //        2, //min neighbors
            //        0, //flags
            //        new Size(200, 200), //minSize
            //        new Size(500, 500) //minSize
            //    );
            face_cascade.detectMultiScale(colorfulImgMat, faces, 1.3, 3, 0, new Size(150,150), new Size(500,500));
            //face_cascade.detectMultiScale(imgMatCinza, faces);
            
            System.out.println(faces.size());
            System.out.println("imagem "+ cont +" - faces:" + faces.size());
            for (int i = 0; i < faces.size(); i++) {
                System.out.println("dkfodsakfosdkops");
                Rect face_i = faces.get(0);     //pega os dados da imagem
                Mat face = new Mat(colorfulImgMat, face_i);
 
                // desenha o retangulo ao redor da face
                rectangle(colorfulImgMat, face_i, new Scalar(0, 255, 0, 1)); 
 
                // deixa as imagens com o mesmo tamanho 
                resize(face, face, new Size(160,160));
                
                if (samples <= totalSamples){
 
                     //salva na pasta a imagem capturada e nomeia no formato com o ID e numero da amostra
                     imwrite("src\\pictures\\person."+ personID + "." + samples + ".jpg", face);
 
                     System.out.print("Foto "+ samples +" capturada \n");
 
                     samples++;
                     //Thread.sleep(300);
                 }
            }
            
            if (tecla == null) {
                tecla = cFrame.waitKey(20);
            }
            
            if (cFrame.isVisible()) {
                System.out.println(capturedFrame);
                //cFrame.showImage(img); 
            }

            cont++;
            if (samples > totalSamples){
                break;
            }
        }
        cFrame.dispose();
    }   
    
}