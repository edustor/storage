package ru.edustor.storage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edustor.storage.model.AccountProfile


@Repository
interface AccountProfileRepository : JpaRepository<AccountProfile, String>

fun AccountProfileRepository.getForAccountId(id: String): AccountProfile {
    return this.findOne(id) ?: let {
        val a = AccountProfile(id)
        this.save(a)
        return@let a
    }
}