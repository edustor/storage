package ru.edustor.storage.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ru.edustor.commons.models.internal.processing.pdf.PdfUploadedEvent
import ru.edustor.commons.models.upload.UploadResult
import ru.edustor.commons.storage.service.BinaryObjectStorageService
import ru.edustor.storage.repository.AccountRepository
import java.io.InputStream
import java.time.Instant
import java.util.*

@Service
class PagesUploadService(val storage: BinaryObjectStorageService,
                         val rabbitTemplate: RabbitTemplate,
                         val accountRepository: AccountRepository) {
    val logger: Logger = LoggerFactory.getLogger(PagesUploadService::class.java)

    fun processFile(uploaderId: String, file: InputStream, fileSize: Long, requestedTarget: String? = null): UploadResult {
        val uploadUuid = UUID.randomUUID().toString()

        logger.info("Processing file $uploadUuid uploaded by $uploaderId")

        val targetLessonId = requestedTarget ?: let {
            val account = accountRepository.findOne(uploaderId)
            val nuTarget = account?.nextUploadTarget ?: return@let null
            account.nextUploadTarget = null
            accountRepository.save(account)
            logger.info("Using target lesson from database: $nuTarget")
            return@let nuTarget
        }

        storage.put(BinaryObjectStorageService.ObjectType.PDF_UPLOAD, uploadUuid, file, fileSize)
        logger.info("PDF $uploadUuid uploaded by $uploaderId")

        val uploadedEvent = PdfUploadedEvent(uploadUuid, uploaderId, Instant.now(), targetLessonId)
        rabbitTemplate.convertAndSend("internal.edustor", "uploaded.pdf.pages.processing", uploadedEvent)

        val result = UploadResult(uploadUuid)
        return result
    }
}