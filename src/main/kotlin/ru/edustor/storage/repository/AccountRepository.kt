package ru.edustor.storage.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.edustor.storage.model.EdustorUploadAccount


@Repository
interface AccountRepository : MongoRepository<EdustorUploadAccount, String>

fun AccountRepository.getForAccountId(id: String): EdustorUploadAccount {
    return this.findOne(id) ?: let {
        val a = EdustorUploadAccount(id)
        this.save(a)
        return@let a
    }
}