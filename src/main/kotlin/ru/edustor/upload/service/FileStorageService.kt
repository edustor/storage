package ru.edustor.upload.service

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
open class FileStorageService(
        @Value("\${S3_URL}") url: String,
        @Value("\${S3_ACCESS_KEY}") accessKey: String,
        @Value("\${S3_SECRET_KEY}") secretKey: String
) {
    private val minio: MinioClient
    val PDF_BUCKET_NAME = "edustor-pdf-uploads"


    init {
        minio = MinioClient(url, accessKey, secretKey)
    }

    open fun putPdf(uuid: String, inputStream: InputStream, size: Long) {
        if (!minio.bucketExists(PDF_BUCKET_NAME)) {
            minio.makeBucket(PDF_BUCKET_NAME)
        }
        minio.putObject(PDF_BUCKET_NAME, "$uuid.pdf", inputStream, size, "application/pdf")
    }
}