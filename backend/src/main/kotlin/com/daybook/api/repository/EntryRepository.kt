package com.daybook.api.repository

import com.daybook.api.domain.enum.EntryType
import com.daybook.api.domain.model.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface EntryRepository : JpaRepository<Entry, Long> {
    fun findByAccountId(accountId: Long): List<Entry>

    fun findByTransactionId(transactionId: Long): List<Entry>

    @Query(
        """
        SELECT COALESCE(SUM(e.amount), 0) 
        FROM Entry e 
        WHERE e.account.id = :accountId 
        AND e.type = :type
    """,
    )
    fun sumAmountByAccountIdAndType(
        accountId: Long,
        type: EntryType,
    ): BigDecimal
}
