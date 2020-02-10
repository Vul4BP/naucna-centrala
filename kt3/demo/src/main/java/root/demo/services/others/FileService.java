package root.demo.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import root.demo.config.MyConfig;
import root.demo.model.Rad;
import root.demo.repository.RadRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileService {

    //private static final String FILE_DIRECTORY = "/var/files";

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    RadRepository radRepository;

    public void storeFile(MultipartFile file) throws IOException {
        Path filePath = Paths.get(MyConfig.file_directory + "/" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("PDF USPESNO SACUVAN");
    }

    public File getFile(String naslovRada){
        Rad rad = radRepository.findOneByNaslov(naslovRada);
        File pdfFile = Paths.get(MyConfig.file_directory + "/" + rad.getPdf()).toFile();
        return pdfFile;
    }
}
