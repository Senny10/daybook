package com.daybook.api.repository

import com.daybook.api.domain.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query("""
        SELECT t FROM Transaction t 
        LEFT JOIN FETCH t.entries e 
        LEFT JOIN FETCH e.account 
        WHERE t.id = :id
    """)
    fun findByIdWithEntries(id: Long): Transaction?

    @Query("""
        SELECT DISTINCT t FROM Transaction t 
        LEFT JOIN FETCH t.entries e 
        LEFT JOIN FETCH e.account 
        ORDER BY t.date DESC
    """)
    fun findAllWithEntries(): List<Transaction>
}
