package ru.edustor.storage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edustor.storage.model.EdustorStorageAccount


@Repository
interface AccountRepository : JpaRepository<EdustorStorageAccount, String>

fun AccountRepository.getForAccountId(id: String): EdustorStorageAccount {
    return this.findOne(id) ?: let {
        val a = EdustorStorageAccount(id)
        this.save(a)
        return@let a
    }
}