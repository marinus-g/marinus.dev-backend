package dev.marinus.backend.controller;

import dev.marinus.backend.security.annotation.RequiresCommand;
import dev.marinus.backend.service.PictureService;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/picture")

public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String id) {
        return this.pictureService.getPicture(id).map(picture -> {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("image/jpg"));
            return ResponseEntity.ok().headers(headers).body(picture);
        }).orElse(ResponseEntity.notFound().build());
    }

    @SneakyThrows
    @PostMapping(path = "/")
    @RequiresCommand("projects")
    public ResponseEntity<String> createPicture(@RequestBody @RequestParam("file") MultipartFile picture) {
        final String pictureUid = this.pictureService.generatePictureUid();
        this.pictureService.createPicture(pictureUid, picture);
        return ResponseEntity.ok(pictureUid);
    }

    @DeleteMapping(path = "/{id}")
    @RequiresCommand("projects")
    public ResponseEntity<?> deletePicture(@PathVariable String id) {
        if (this.pictureService.pictureExists(id)) {
            this.pictureService.deletePicture(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
