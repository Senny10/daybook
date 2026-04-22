package com.daybook.api.domain.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "transactions")
class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val date: LocalDate,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false, unique = true)
    val reference: String,

    @OneToMany(mappedBy = "transaction", cascade = [CascadeType.ALL])
    val entries: List<Entry> = emptyList()
)