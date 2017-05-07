package ru.edustor.storage.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account_profiles")
class AccountProfile() {
    @Id lateinit var accountId: String
    var nextUploadTarget: String? = null

    constructor(accountId: String) : this() {
        this.accountId = accountId
    }
}