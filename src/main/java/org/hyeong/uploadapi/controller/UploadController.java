package org.hyeong.uploadapi.controller;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.hyeong.uploadapi.exception.UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequestMapping("/upload/api")
public class UploadController {

    @Value("${org.hyeong.partTimer.profile}")
    private String uploadFolder_partTimer_profile;

    @Value("${org.hyeong.partTimer.document}")
    private String uploadFolder_partTImer_Document;

    @Value("${org.hyeong.complaints}")
    private String uploadFolder_complaints;

    @Value("${org.hyeong.jobPosting}")
    private String uploadFolder_jobPosting;

    @PostMapping("partTimer/profile")
    public ResponseEntity<List<String>> uploadFiles_partTimer_profile (MultipartFile[] files) {

        return uploadWithThumbnail(files, uploadFolder_partTimer_profile);
    }

    @PostMapping("partTimer/document")
    public ResponseEntity<List<String>> uploadFiles_partTImer_Document (MultipartFile[] files) {

        return upload(files, uploadFolder_partTImer_Document);
    }

    @PostMapping("complaints")
    public ResponseEntity<List<String>> uploadFiles_complaints (MultipartFile[] files) {

        return upload(files, uploadFolder_complaints);
    }

    @PostMapping("jobPosting")
    public ResponseEntity<List<String>> uploadFiles_jobPosting (MultipartFile[] files) {

        return uploadWithThumbnail(files, uploadFolder_jobPosting);
    }


    private ResponseEntity<List<String>> upload(MultipartFile[] files, String folderName) {

        if(files == null || files.length == 0){
            return ResponseEntity.noContent().build();
        }


        List<String> uploadedFiles = new ArrayList<>();

        for(MultipartFile file : files){

            String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();

            String fileName = UUID.randomUUID().toString() + originalFilename;

            try {

                File savedFile = new File(folderName, fileName);
                FileCopyUtils.copy(file.getBytes(), savedFile);

                uploadedFiles.add(fileName);

            } catch (IOException e) {

                e.printStackTrace();
                throw new UploadException(e.getMessage());
            }
        }

        return ResponseEntity.ok(uploadedFiles);
    }

    private ResponseEntity<List<String>> uploadWithThumbnail(MultipartFile[] files, String folderName) {

        if(files == null || files.length == 0){
            return ResponseEntity.noContent().build();
        }


        List<String> uploadedFiles = new ArrayList<>();

        for(MultipartFile file : files){

            String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();

            String fileName = UUID.randomUUID().toString() + originalFilename;

            try {

                File savedFile = new File(folderName, fileName);
                FileCopyUtils.copy(file.getBytes(), savedFile);

                if(file.getContentType().startsWith("image")){

                    String thumbnailFileName = "s_" + fileName;

                    @Cleanup
                    InputStream inputStream = new FileInputStream(new File(folderName, fileName));
                    @Cleanup
                    OutputStream outputStream = new FileOutputStream(new File(folderName, thumbnailFileName));

                    Thumbnailator.createThumbnail(inputStream, outputStream, 200, 200);
                }

                uploadedFiles.add(fileName);

            } catch (IOException e) {

                e.printStackTrace();
                throw new UploadException(e.getMessage());
            }
        }

        return ResponseEntity.ok(uploadedFiles);
    }
}
