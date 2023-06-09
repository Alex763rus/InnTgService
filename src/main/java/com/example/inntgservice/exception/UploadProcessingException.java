package com.example.inntgservice.exception;

public class UploadProcessingException extends RuntimeException {

    private static final String UPLOAD_PROCESSING_EXCEPTION = "Ошибка во время загрузки файла: ";

    public UploadProcessingException(String message) {
        super(UPLOAD_PROCESSING_EXCEPTION + message);
    }
}
