package zipgoo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.domain.Review;

import java.io.IOException;

@Repository
public interface StorageRepository{
    String upload(MultipartFile multipartFiles);

    void delete(String fileName);
}
