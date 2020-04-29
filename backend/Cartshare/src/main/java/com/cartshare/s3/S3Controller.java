package com.cartshare.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cartshare.QRCode.QRCodeGenerator;
import com.cartshare.QRCode.QRCodeReader;
import com.cartshare.s3.s3Service;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/storage")
public class s3Controller {
	@Autowired
	private s3Service s3Client;
	
	@Autowired
	private QRCodeGenerator QR;
	
	@Autowired
	private QRCodeReader QRReader;

//    @Autowired
//    s3Controller (s3DAO s3Client,QRCodeGenerator QR) {
//        this.s3Client = s3Client;
//        this.QR=QR;
//    }
	 @PostMapping("/decodeQRCode")
	    public String decodeQRCode(@RequestParam("File") MultipartFile orderQR)
	 {
	    	System.out.println("Inside QR decoder");
	    	System.out.println();
	    	String decodedText=null;
	    	
	    	 try {
	             
	            decodedText = QRReader.decodeQRCode(orderQR);
	             if(decodedText == null) {
	                 System.out.println("No QR Code found in the image");
	             } else {
	                 System.out.println("Decoded text = " + decodedText);
	             }
	         } catch (IOException e) {
	             System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
	         }
		
	    	return decodedText;
	    }
	 
    @GetMapping("/generateQRCode")
    public byte[] generateQRCode(@RequestHeader("orderid") String orderid) {
    	System.out.println("Inside QR generator file");
    	System.out.println("Header"+orderid);
    	StringBuilder sb=new StringBuilder("./");
    		sb.append(orderid);
    		sb.append(".png");
    		 byte[] img=null;
    	 try {
            img =  QR.generateQRCodeImage(orderid, 350, 350, sb.toString());
         } catch (WriterException e) {
             System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
         } catch (IOException e) {
             System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
         }
		return img;
    }
    
   
    
    
    @PostMapping("/uploadFiletoS3")
    public String uploadFile( @RequestParam("File") MultipartFile file) {
    	System.out.println("Inside upload file");
    	System.out.println("Content" +file.getContentType());
    	System.out.println("Name"  + file.getName());
    	return s3Client.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestParam("url") String fileUrl) {
        return this.s3Client.removeFromS3Bucket(fileUrl);
    }

}

