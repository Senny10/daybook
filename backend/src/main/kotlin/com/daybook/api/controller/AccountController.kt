package com.daybook.api.controller

import com.daybook.api.dto.request.CreateAccountRequest
import com.daybook.api.dto.response.AccountResponse
import com.daybook.api.dto.response.BalanceResponse
import com.daybook.api.service.AccountService
import com.daybook.api.service.LedgerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,
    private val ledgerService: LedgerService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(
        @Valid @RequestBody request: CreateAccountRequest,
    ): AccountResponse {
        val account = accountService.createAccount(
            name = request.name,
            type = request.type,
            description = request.description
        )
        return AccountResponse.from(account)
    }

    @GetMapping
    fun getAllAccounts(): List<AccountResponse> = accountService.getAllAccounts().map { AccountResponse.from(it) }

    @GetMapping("/{id}")
    fun getAccountById(
        @PathVariable id: Long,
    ): AccountResponse = AccountResponse.from(accountService.getAccountById(id))

    @GetMapping("/{id}/balance")
    fun getBalance(
        @PathVariable id: Long,
    ): BalanceResponse {
        val account = accountService.getAccountById(id)
        val balance = ledgerService.getAccountBalance(id)
        return BalanceResponse(
            accountId = id,
            accountName = account.name,
            balance = balance,
        )
    }
}
