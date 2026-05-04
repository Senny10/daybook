package com.daybook.api.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/config")
class ConfigController {

    @Value("\${daybook.registration.public:false}")
    private var publicRegistrationEnabled: Boolean = false

    @GetMapping
    fun getConfig(): Map<String, Any> = mapOf(
        "publicRegistrationEnabled" to publicRegistrationEnabled,
        "version" to "1.0.0"
    )
}