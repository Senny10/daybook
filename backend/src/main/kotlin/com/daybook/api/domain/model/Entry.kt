package com.daybook.api.domain.model

import com.daybook.api.domain.enum.EntryType
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "entries")
class Entry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    val transaction: Transaction,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,
    @Column(nullable = false, precision = 19, scale = 4)
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: EntryType,
)
