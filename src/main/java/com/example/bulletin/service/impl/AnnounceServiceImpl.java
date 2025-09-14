package com.example.bulletin.service.impl;

import com.example.bulletin.dao.AnnFileDao;
import com.example.bulletin.dao.AnnounceDao;
import com.example.bulletin.entity.AnnFile;
import com.example.bulletin.entity.Announce;
import com.example.bulletin.service.AnnounceService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AnnounceServiceImpl implements AnnounceService {

    private final int PAGE_SIZE = 5;

    @Value("${UPLOAD_PATH}")
    private String uploadDir;


    @Autowired
    private AnnounceDao announceDao;

    @Autowired
    private AnnFileDao annFileDao;

    @Override
    public List<Announce> getAll() {
        return announceDao.findAll();
    }

    @Override
    public List<Announce> getAnnounceByPage(int page) {
        int totalPage = getTotalPage();
        if (totalPage == 0) totalPage = 1;
        if (page <= 0) page = 1;
        else if (page > totalPage) page = totalPage;
        return announceDao.findAllByPage(page, PAGE_SIZE);
    }

    @Override
    public Announce getAnnounceById(Long id) {
        return announceDao.findById(id);
    }

    @Override
    public void create(Announce announce, MultipartFile[] attachments) {
        if (StringUtils.isBlank(announce.getPublisher())) announce.setPublisher("Administrator");
        announce.setContent(Jsoup.clean(announce.getContent(), Safelist.basic()));
        announceDao.create(announce);
        uploadFiles(announce.getId(), attachments);
    }


    @Override
    public void update(Announce announce, MultipartFile[] attachments) {
        announce.setContent(Jsoup.clean(announce.getContent(), Safelist.basic()));
        announceDao.update(announce);
        uploadFiles(announce.getId(), attachments);
    }

    @Override
    public void deleteAnnounceById(Long id) {
        announceDao.deleteById(id);
    }

    @Override
    public List<AnnFile> getAllFilesByAnnId(Long id) {
        if (id == null) return Collections.emptyList();
        return annFileDao.findAllByAnnId(id);
    }

    @Override
    public void deleteFile(Long annId, Long fileId) {
        AnnFile annFile = annFileDao.findByAnnIdAndFileId(annId, fileId);
        if (annFile == null) {
            return;
        }
        try {
            Path p = Paths.get(uploadDir, "ann", String.valueOf(annId), annFile.getStoredName())
                    .toAbsolutePath().normalize();
            Files.deleteIfExists(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        annFileDao.delete(annFile);
    }

    @Override
    public int getTotalPage() {
        int totalPages = (int) Math.ceil((double) announceDao.countAll() / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        return totalPages;
    }

    @Override
    public ResponseEntity<InputStreamResource> downFiles(Long annId, Long filesId) throws IOException {
        AnnFile annFile = annFileDao.findByAnnIdAndFileId(annId, filesId);
        if (annFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Path file = Paths.get(uploadDir, "ann", String.valueOf(annId), annFile.getStoredName())
                .normalize();
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ContentDisposition cd = ContentDisposition.attachment()
                .filename(annFile.getOriginalName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .contentLength(Files.size(file))
                .body(new InputStreamResource(Files.newInputStream(file)));

    }

    private void uploadFiles(Long annId, MultipartFile[] attachments) {
        for (MultipartFile file : attachments) {
            if (file.isEmpty()) continue;
            try {
                Path dir = Paths.get(uploadDir, "ann", String.valueOf(annId));
                Files.createDirectories(dir);
                String safeName = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[\\\\/:*?\"<>|]", "_");
                Path dest = dir.resolve(safeName);
                file.transferTo(dest.toFile());


                AnnFile annFile = new AnnFile();
                annFile.setAnnounceId(annId);
                annFile.setOriginalName(file.getOriginalFilename());
                annFile.setStoredName(safeName);

                annFileDao.save(annFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
