package dev.marinus.backend.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class PictureService {

    @SneakyThrows
    public void deletePicture(String pictureUid) {
        Files.deleteIfExists(Paths.get("pictures/" + pictureUid));
    }

    @SneakyThrows
    public void createPicture(String pictureUid, byte[] picture) {
        Files.write(Paths.get("pictures/" + pictureUid), picture);
    }

    @SneakyThrows
    public Optional<byte[]> getPicture(String pictureUid) {
        return pictureExists(pictureUid) ? Optional.of(Files.readAllBytes(Paths.get("pictures/" + pictureUid))) : Optional.empty();
    }

    public boolean pictureExists(String pictureUid) {
        return Files.exists(Paths.get("pictures/" + pictureUid));
    }

    public String generatePictureUid() {
        String pictureUid;
        do {
            pictureUid = Long.toHexString(Double.doubleToLongBits(Math.random()));
        } while (pictureExists(pictureUid));
        return pictureUid;
    }

    public String determineContentType(byte[] picture) {
        if (picture[0] == (byte) 0xFF && picture[1] == (byte) 0xD8) {
            return "image/jpeg";
        } else if (picture[0] == (byte) 0x89 && picture[1] == (byte) 0x50 && picture[2] == (byte) 0x4E && picture[3] == (byte) 0x47) {
            return "image/png";
        } else if (picture[0] == (byte) 0x47 && picture[1] == (byte) 0x49 && picture[2] == (byte) 0x46) {
            return "image/gif";
        } else if (picture[0] == (byte) 0x42 && picture[1] == (byte) 0x4D) {
            return "image/bmp";
        } else if ((picture[0] == (byte) 0x49 && picture[1] == (byte) 0x20 && picture[2] == (byte) 0x49) ||
                (picture[0] == (byte) 0x4D && picture[1] == (byte) 0x4D && picture[2] == (byte) 0x00)) {
            return "image/tiff";
        } else {
            return "unknown";
        }
    }
}