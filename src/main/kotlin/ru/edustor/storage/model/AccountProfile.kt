package ru.edustor.storage.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account_profiles")
data class AccountProfile(
        var nextUploadTarget: String? = null,
        @Id val accountId: String
)