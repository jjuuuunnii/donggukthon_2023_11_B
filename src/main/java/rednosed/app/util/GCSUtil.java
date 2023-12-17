package rednosed.app.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
@Slf4j
public class GCSUtil {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String BUCKET_NAME;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String PROJECT_ID;

    @Value("${spring.cloud.gcp.storage.bucket-dir-path}")
    private String DIR_PATH;

    @Value("${spring.cloud.gcp.storage.stamp-dir}")
    private String STAMP_DIR;

    @Value("${spring.cloud.gcp.storage.seal-dir}")
    private String SEAL_DIR;

    private final String T_SEAL = "seal";
    private final String T_STAMP = "stamp";

//    @Value("${spring.cloud.gcp.credentials.location}")
//    private String CREDENTIALS_KEY;


    public String saveFileImageToGCS(File image, String type) throws IOException {
        String objectName = UUID.randomUUID() + ".jpg";
        Storage storage = getStorage();
        BlobId blobId;
        String fullPath;

        if (type.equals(T_STAMP)) {
            blobId = BlobId.of(BUCKET_NAME + "/" +  STAMP_DIR, objectName);
            fullPath = DIR_PATH + STAMP_DIR;
        } else {
            blobId = BlobId.of(BUCKET_NAME + "/" + SEAL_DIR, objectName);
            fullPath = DIR_PATH + SEAL_DIR;

        }
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        try (WriteChannel writer = storage.writer(blobInfo);
             InputStream inputStream = new FileInputStream(image)) {
            byte[] buffer = new byte[1024];
            int limit;
            while ((limit = inputStream.read(buffer)) >= 0) {
                writer.write(ByteBuffer.wrap(buffer, 0, limit));
            }
        }
        return fullPath;
    }


    public void deleteImageFromGCS(String fullPath, String type) throws URISyntaxException, IOException {
        String objectName = extractFileName(fullPath);
        Storage storage = getStorage();

        Blob blob;
        if(type.equals(T_STAMP)){
            blob = storage.get(BUCKET_NAME + "/" + T_STAMP, objectName);
        } else {
            blob = storage.get(BUCKET_NAME + "/" + T_SEAL, objectName);
        }

        if (blob == null) {
            log.info("The object " + objectName + " wasn't found in " + BUCKET_NAME);
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

        if(type.equals(T_STAMP)){
            storage.delete(BUCKET_NAME + "/" + T_STAMP, objectName, precondition);
        } else {
            storage.delete(BUCKET_NAME + "/" + T_SEAL, objectName, precondition);
        }
    }

    public String extractFileName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private Storage getStorage() throws IOException {
        ClassPathResource resource = new ClassPathResource("striped-bonfire-405812-4dfb61d784ed.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        return StorageOptions.newBuilder().setProjectId(PROJECT_ID).setCredentials(credentials).build().getService();
    }
}
