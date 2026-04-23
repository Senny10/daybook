package com.daybook.api.repository

import com.daybook.api.domain.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByReference(reference: String): Transaction?

    fun findByDateBetween(
        start: LocalDate,
        end: LocalDate,
    ): List<Transaction>

    fun existsByReference(reference: String): Boolean
}
