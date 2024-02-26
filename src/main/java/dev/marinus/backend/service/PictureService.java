package dev.marinus.backend.service;

import dev.marinus.backend.repository.ProjectRepository;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class PictureService {

    private final ProjectRepository projectRepository;

    public PictureService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @SneakyThrows
    public void deletePicture(String pictureUid) {
        this.findPicture(pictureUid).ifPresent(File::delete);
    }

    @SneakyThrows
    public void createPicture(String pictureUid, MultipartFile picture) {
        if (picture == null || picture.getOriginalFilename() == null || picture.getOriginalFilename().isBlank())
            return;
        final int length = picture.getOriginalFilename().split("\\.").length;
        final String extension = length > 1 ? picture.getOriginalFilename().split("\\.")[length - 1] : "png";
        final File file = new File("pictures/" + pictureUid + "." + extension);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(file.toPath(), picture.getBytes());
    }

    @Scheduled(fixedDelay = 60, initialDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void deleteUnusedImages() {
        File directory = new File("pictures");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                final String[] splitted = file.getName().split("\\.");
                int lengthToRemove = splitted[splitted.length - 1].length() + 1;
                final String pictureUid = file.getName().substring(0, file.getName().length() - lengthToRemove);
                if (this.projectRepository.existsByThumbnailReference(pictureUid)) {
                    continue;
                }
                file.delete();
            }
        }
    }

    @SneakyThrows
    public Optional<byte[]> getPicture(String pictureUid) {
        return this.findPicture(pictureUid).map(file -> {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                return null;
            }
        }).filter(bytes -> bytes.length > 0);
    }

    public boolean pictureExists(String pictureUid) {
        File directory = new File("pictures");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(pictureUid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Optional<File> findPicture(String pictureUid) {
        File directory = new File("pictures");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(pictureUid)) {
                    return Optional.of(file);
                }
            }
        }
        return Optional.empty();
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