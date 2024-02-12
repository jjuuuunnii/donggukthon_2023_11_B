package rednosed.app.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;


import java.io.File;
import java.io.IOException;

@Component
public class MultipartFileToFileConverter implements Converter<MultipartFile, File> {

    @Override
    public File convert(MultipartFile source) {
        try {

            File tempFile = File.createTempFile("temp", null);
            source.transferTo(tempFile);
            return tempFile;

        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
