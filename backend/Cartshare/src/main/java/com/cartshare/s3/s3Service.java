package com.cartshare.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service("s3")
public class s3Service {
	
	
	private AmazonS3 s3client;

    @Value("https://s3.us-east-1.amazonaws.com")
    private String endpointUrl;
    @Value("${aws.s3.default.bucket}")
    private String S3bucketName;
    
    @Value("${aws.access.key.id}")
    private String accessKey;
    @Value("${aws.access.key.secret}")
    private String secretKey;

    @SuppressWarnings("deprecation")
	@PostConstruct
    private void initializeAmazon() {
        AWSCredentials AWSS3Login = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(AWSS3Login);
    }

    public String uploadFile(MultipartFile uploadedFile) {
    	System.out.println("Inside upload file");
        String fileUrlInS3 = "";
        try {
            File file = convertMultiPartToFile(uploadedFile);
            
            
            String fileName = assignNametoFile(uploadedFile);
            
            
            // can save it using the product name or user's as URL is generated accordingly
            fileUrlInS3 = endpointUrl + "/" + S3bucketName + "/" + fileName;
            uploadTos3Bucket(fileName.trim(), file);
            file.delete();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return fileUrlInS3;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @SuppressWarnings("deprecation")
	private String assignNametoFile(MultipartFile file) {
        return new Date().getDate()+ "-" + file.getOriginalFilename().replace(" ", "*").trim();
       
    }

    private void uploadTos3Bucket(String fileName, File file) {
    	System.out.println("filename"+fileName);
    	
        s3client.putObject(new PutObjectRequest(S3bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
               
    }

    public String removeFromS3Bucket(String UrlOfFile) {
        String fileName = UrlOfFile.substring(UrlOfFile.lastIndexOf("/") + 1);
       
        System.out.println("before in the remove part");
        System.out.println(s3client.doesObjectExist(S3bucketName,fileName));
        s3client.getObject(S3bucketName, fileName);
        s3client.deleteObject(new DeleteObjectRequest(S3bucketName, fileName));
        
        System.out.println("after in the remove part");
        System.out.println(s3client.doesObjectExist(S3bucketName, fileName));
        return "Successfully deleted from s3 bucket";
    }
	
	
	
	
	
	
	
	
	

	
	

   


}

