package org.hyeong.uploadapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hyeong.uploadapi.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/upload/api")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

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

        return uploadService.uploadWithThumbnail(files, uploadFolder_partTimer_profile);
    }

    @PostMapping("partTimer/document")
    public ResponseEntity<List<String>> uploadFiles_partTImer_Document (MultipartFile[] files) {

        return uploadService.upload(files, uploadFolder_partTImer_Document);
    }

    @PostMapping("complaints")
    public ResponseEntity<List<String>> uploadFiles_complaints (MultipartFile[] files) {

        return uploadService.upload(files, uploadFolder_complaints);
    }

    @PostMapping("jobPosting")
    public ResponseEntity<List<String>> uploadFiles_jobPosting (MultipartFile[] files) {

        return uploadService.uploadWithThumbnail(files, uploadFolder_jobPosting);
    }
}
