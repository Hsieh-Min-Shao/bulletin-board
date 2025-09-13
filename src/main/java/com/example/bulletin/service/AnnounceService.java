package com.example.bulletin.service;

import com.example.bulletin.entity.AnnFile;
import com.example.bulletin.entity.Announce;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnnounceService {
    List<Announce> getAll();
    List<Announce> getAnnounceByPage(int page);
    Announce getAnnounceById(Long id);
    void create(Announce announce, MultipartFile[] attachments);
    void update(Announce announce,MultipartFile[] attachments);
    void deleteAnnounceById(Long id);
    List<AnnFile> getAllFilesByAnnId(Long id);

    void deleteFile(Long annId, Long fileId);

    int getTotalPage();

    ResponseEntity<InputStreamResource> downFiles(Long annId, Long filesId) throws IOException;
}
