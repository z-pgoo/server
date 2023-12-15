package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;


public interface StorageRepository{
    String upload(MultipartFile multipartFiles);

    String deleteByFileName(String fileName);
}
