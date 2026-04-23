package com.daybook.api.service

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.model.Account
import com.daybook.api.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {
    fun createAccount(
        name: String,
        type: AccountType,
        description: String = "",
    ): Account {
        if (accountRepository.existsByName(name)) {
            throw IllegalArgumentException(
                "Account with name '$name' already exists",
            )
        }
        return accountRepository.save(
            Account(name = name, type = type, description = description),
        )
    }

    fun getAllAccounts(): List<Account> = accountRepository.findAll()

    fun getAccountById(id: Long): Account =
        accountRepository
            .findById(id)
            .orElseThrow {
                IllegalArgumentException("Account not found with id: $id")
            }

    fun getAccountsByType(type: AccountType): List<Account> = accountRepository.findByType(type)
}
