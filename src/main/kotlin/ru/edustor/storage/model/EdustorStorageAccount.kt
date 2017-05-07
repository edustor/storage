package ru.edustor.storage.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class EdustorStorageAccount() {
    @Id lateinit var id: String
    var nextUploadTarget: String? = null

    constructor(id: String) : this() {
        this.id = id
    }
}