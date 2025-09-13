package com.example.bulletin.controller;

import com.example.bulletin.entity.AnnFile;
import com.example.bulletin.entity.Announce;
import com.example.bulletin.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class AnnounceController {


    @Autowired
    private AnnounceService announceService;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page, Model model) {
        int totalPage = announceService.getTotalPage();

        if (page < 1) {
            return "redirect:/?page=1";
        } else if (page > totalPage) {
            return "redirect:/?page=" + totalPage;
        }

        List<Announce> list = announceService.getAnnounceByPage(page);
        model.addAttribute("announces", list);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        return "homePage";
    }

    @GetMapping("/announce/new")
    public String newForm(Model model) {
        model.addAttribute("announce", new Announce());
        model.addAttribute("isCreate", true);
        return "form";
    }

    @GetMapping("/announce/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Announce announce = announceService.getAnnounceById(id);
        if (announce == null) return "redirect:/";
        model.addAttribute("announce", announce);
        model.addAttribute("isCreate", false);
        model.addAttribute("annFiles", announceService.getAllFilesByAnnId(id));
        return "form";
    }

    @PostMapping("/announce/create")
    public String create(@ModelAttribute Announce announce,
                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        announceService.create(announce, attachments);
        return "redirect:/";
    }

    @PostMapping("/announce/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Announce announce,
                         @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
        announce.setId(id);
        announceService.update(announce, attachments);
        return "redirect:/";
    }

    @DeleteMapping("/announce/{annId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long annId) {
        announceService.deleteAnnounceById(annId);
    }

    @GetMapping("/announce/{annId}/files")
    @ResponseBody
    public List<AnnFile> getFiles(@PathVariable Long annId) {
        return announceService.getAllFilesByAnnId(annId);
    }

    @GetMapping("/announce/{annId}/files/download/{filesId}")
    public ResponseEntity<InputStreamResource> downFiles(@PathVariable Long annId, @PathVariable Long filesId) throws IOException {
        return announceService.downFiles(annId,filesId);
    }

    @DeleteMapping("/announce/{annId}/files/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable Long annId, @PathVariable Long fileId) {
        announceService.deleteFile(annId, fileId);
    }
}
