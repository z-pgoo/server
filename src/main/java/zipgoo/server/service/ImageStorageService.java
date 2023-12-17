package zipgoo.server.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zipgoo.server.dto.ImageUploadResponse;
import zipgoo.server.exception.BadRequestException;
import zipgoo.server.exception.InternalServerException;
import zipgoo.server.repository.StorageRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageStorageService {
    private static final int MAX_IMAGE_FILE_LENGTH = 10;
    private final StorageRepository storageRepository;
    private final Executor executor;
    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;
    public ImageUploadResponse uploadFiles(MultipartFile[] imageFiles){
        validate(imageFiles);
        List<CompletableFuture<String>> futureFiles = Arrays.stream(imageFiles)
                .map(file -> CompletableFuture.supplyAsync(()->storageRepository.upload(file), executor)).toList();
        List<String> fileNames = getFileNamesFromFutures(futureFiles);
        return convertToImageUploadResponse(fileNames);
    }

    private List<String> getFileNamesFromFutures(List<CompletableFuture<String>> futureFiles) {
        List<String> fileNames = new ArrayList<>();
        AtomicBoolean catchException = new AtomicBoolean(false);
        futureFiles.forEach(future->{
            try {
                fileNames.add(future.join());
            } catch (CompletionException e) {
                catchException.set(true);
            }
        });
        handleException(catchException, fileNames);
        return fileNames;
    }

    private void handleException(AtomicBoolean catchException, List<String> fileNames) {
        if (catchException.get()) {
            executor.execute(() -> deleteFiles(fileNames));
            throw new InternalServerException("이미지 업로드시 예외가 발생하였습니다.");
        }
    }

    public void deleteFiles(List<String> fileNames) {
        fileNames.stream()
                .parallel()
                .forEach(storageRepository::delete);
    }

    private void validate(MultipartFile[] imageFiles) {
        if(imageFiles.length > MAX_IMAGE_FILE_LENGTH){
            throw new BadRequestException("파일 수가 10개를 초과하였습니다.");
        }
        Arrays.stream(imageFiles)
                .forEach(this::validateImageFile);
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.info("잘못된 이미지 형식입니다. 현재 이미지 형식 {}", contentType);
            throw new BadRequestException("image 형식이 아닙니다.");
        }
    }

    private ImageUploadResponse convertToImageUploadResponse(List<String> fileNames) {
        List<String> urls = fileNames.stream()
                .map(fileName -> baseUrl + fileName)
                .toList();
        return new ImageUploadResponse(urls, fileNames);
    }
}
