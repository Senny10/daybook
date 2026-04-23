package com.daybook.api.dto.response

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.model.Account

data class AccountResponse(
    val id: Long,
    val name: String,
    val type: AccountType,
    val description: String,
) {
    companion object {
        fun from(account: Account) =
            AccountResponse(
                id = account.id!!,
                type = account.type,
                name = account.name,
                description = account.description,
            )
    }
}
