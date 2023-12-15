package zipgoo.server.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ImageStorageService {
    private static final int MAX_IMAGE_FILE_LENGTH = 10;
    private final StorageRepository storageRepository;
    private final Executor executor;
    public List<String> uploadFiles(MultipartFile[] imageFiles){
        validate(imageFiles);
        List<CompletableFuture<String>> futureFiles = Arrays.stream(imageFiles)
                .map(file -> CompletableFuture.supplyAsync(()->storageRepository.upload(file), executor)).toList();
        List<String> fileNames = getFileNamesFromFutures(futureFiles);
        return fileNames;
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

    private void deleteFiles(List<String> fileNames) {
        fileNames.stream()
                .parallel()
                .forEach(storageRepository::deleteByFileName);
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
        if (contentType == null || contentType.startsWith("image/")) {
            throw new BadRequestException("image 형식이 아닙니다.");
        }
    }

}
