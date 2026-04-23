package com.daybook.api.repository

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByName(name: String): Account?

    fun findByType(type: AccountType): List<Account>

    fun existsByName(name: String): Boolean
}
