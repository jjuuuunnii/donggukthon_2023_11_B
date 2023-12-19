package rednosed.app.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import rednosed.app.contrant.Constants;
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


//    @Value("${spring.cloud.gcp.credentials.location}")
//    private String CREDENTIALS_KEY;


    public String saveFileImageToGCS(File image, String type) throws IOException {
        Storage storage = getStorage();
        String objectName;

        if (type.equals(Constants.T_STAMP)) {
            objectName = UUID.randomUUID() + "_STAMP" + ".jpg";
        } else {
            objectName = UUID.randomUUID() + "_SEAL" + ".jpg";
        }

        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        String fullPath = DIR_PATH + objectName;
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
        blob = storage.get(BUCKET_NAME, objectName);

        if (blob == null) {
            log.info("The object " + objectName + " wasn't found in " + BUCKET_NAME);
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());


        storage.delete(BUCKET_NAME, objectName, precondition);
    }

    public String extractFileName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private Storage getStorage() throws IOException {
        ClassPathResource resource = new ClassPathResource("red-nose-project-d487e1027b27.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        return StorageOptions.newBuilder().setProjectId(PROJECT_ID).setCredentials(credentials).build().getService();
    }
}
