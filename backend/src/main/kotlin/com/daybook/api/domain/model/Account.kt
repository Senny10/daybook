package com.daybook.api.domain.model

import com.daybook.api.domain.enum.AccountType
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class Account(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: AccountType,

    @Column(nullable = false)
    val description: String = ""
)