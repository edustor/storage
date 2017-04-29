package ru.edustor.storage.controller

import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import ru.edustor.commons.api.CoreApi
import ru.edustor.commons.exceptions.http.DataFetchFailedException
import ru.edustor.commons.storage.service.BinaryObjectStorageService
import ru.edustor.storage.exception.NoPagesFoundException
import ru.edustor.storage.exception.PageNotFoundException
import java.io.ByteArrayOutputStream

@RestController
class PdfDownloadController(val coreApi: CoreApi, val storage: BinaryObjectStorageService) {

    val logger: Logger = LoggerFactory.getLogger(PdfDownloadController::class.java)

    @ResponseBody
    @RequestMapping("/pdf/{lessonId}", produces = arrayOf("application/pdf"))
    fun getPdf(@PathVariable lessonId: String): HttpEntity<ByteArray> {
        val resp = coreApi.getLessonPageFiles(lessonId).execute()
        if (!resp.isSuccessful) {
            throw DataFetchFailedException("Failed to get files list: Core returned ${resp.errorBody()}")
        }

        val fileIds = resp.body()
        if (fileIds.isEmpty()) {
            throw NoPagesFoundException()
        }

        val document = com.itextpdf.text.Document()
        val outputStream = ByteArrayOutputStream()
        val copy = PdfCopy(document, outputStream)
        document.open()
        document.addTitle(lessonId)

        fileIds.forEach {
            val pageStream = (storage.get(BinaryObjectStorageService.ObjectType.PAGE, it)
                    ?: throw PageNotFoundException("Cannot find page file: $it"))
            val pdfReader = PdfReader(pageStream)
            copy.addDocument(pdfReader)
        }

        document.close()

        logger.info("Accessing lesson PDF: $lessonId")

        return HttpEntity(outputStream.toByteArray())
    }
}