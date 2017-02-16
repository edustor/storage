package ru.edustor.upload.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class EdustorUploadAccount() {
    @Id lateinit var id: String
    var nextUploadTarget: String? = null

    constructor(id: String) : this() {
        this.id = id
    }
}