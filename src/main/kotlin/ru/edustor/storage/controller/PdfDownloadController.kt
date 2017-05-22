package ru.edustor.storage.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import ru.edustor.commons.storage.service.BinaryObjectStorageService
import ru.edustor.commons.storage.service.BinaryObjectStorageService.ObjectType.ASSEMBLED_DOCUMENT
import ru.edustor.storage.exception.DocumentNotFoundException

@RestController
class PdfDownloadController(val storage: BinaryObjectStorageService) {

    val logger: Logger = LoggerFactory.getLogger(PdfDownloadController::class.java)

    @ResponseBody
    @RequestMapping("/pdf/{documentId}")
    fun getPdf(@PathVariable documentId: String): ResponseEntity<InputStreamResource> {
        val lessonInputStream = storage.get(ASSEMBLED_DOCUMENT, documentId) ?: throw DocumentNotFoundException()
        val objStat = storage.stat(ASSEMBLED_DOCUMENT, documentId)
        val inputStreamResource = InputStreamResource(lessonInputStream)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.contentLength = objStat.length

        logger.info("Accessing assembled document PDF: $documentId")

        return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
    }
}