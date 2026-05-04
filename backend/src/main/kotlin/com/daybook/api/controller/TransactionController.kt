package com.daybook.api.controller

import com.daybook.api.dto.request.CreateTransactionRequest
import com.daybook.api.dto.response.TransactionDetailResponse
import com.daybook.api.dto.response.TransactionResponse
import com.daybook.api.service.LedgerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val ledgerService: LedgerService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postTransaction(
        @Valid @RequestBody request: CreateTransactionRequest
    ): TransactionResponse {
        val transaction = ledgerService.postTransaction(
            date = request.date,
            description = request.description,
            reference = request.reference,
            entryRequests = request.entries.map {
                LedgerService.EntryRequest(
                    accountId = it.accountId,
                    amount = it.amount,
                    type = it.type
                )
            }
        )
        return TransactionResponse.from(transaction)
    }

    @GetMapping
    fun getAllTransactions(): List<TransactionDetailResponse> =
        ledgerService.getAllTransactions()
            .map { TransactionDetailResponse.from(it) }

    @GetMapping("/{id}")
    fun getTransactionById(
        @PathVariable id: Long
    ): TransactionDetailResponse =
        TransactionDetailResponse.from(
            ledgerService.getTransactionById(id)
        )
}
