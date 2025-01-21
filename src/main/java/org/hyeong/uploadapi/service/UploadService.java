package org.hyeong.uploadapi.service;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.hyeong.uploadapi.exception.UploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    //폴더 없으면 생성
    private void ensureFolderExists(String folderPath) {

        File folder = new File(folderPath);

        if (!folder.exists()) {

            boolean isCreated = folder.mkdirs();

            if (!isCreated) {
                throw new UploadException("Failed to create upload folder: " + folderPath);
            }
        }
    }


    public ResponseEntity<List<String>> upload(MultipartFile[] files, String folderName) {

        if(files == null || files.length == 0){
            return ResponseEntity.noContent().build();
        }

        ensureFolderExists(folderName); // 폴더 확인 및 생성

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

    public ResponseEntity<List<String>> uploadWithThumbnail(MultipartFile[] files, String folderName) {

        if(files == null || files.length == 0){
            return ResponseEntity.noContent().build();
        }

        ensureFolderExists(folderName); // 폴더 확인 및 생성

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
