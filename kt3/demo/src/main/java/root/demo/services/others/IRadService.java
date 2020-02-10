package root.demo.services.others;

import org.springframework.web.multipart.MultipartFile;
import root.demo.model.Rad;

public interface IRadService {
    public Rad addRad(Rad rad, MultipartFile file, String autor);
    public Rad editRad(Rad rad, MultipartFile file);
}
