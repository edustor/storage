package ru.edustor.upload.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edustor.upload.model.EdustorUploadAccount


@Repository
interface AccountRepository : JpaRepository<EdustorUploadAccount, String>

fun AccountRepository.getForAccountId(id: String): EdustorUploadAccount {
    return this.findOne(id) ?: let {
        val a = EdustorUploadAccount(id)
        this.save(a)
        return@let a
    }
}