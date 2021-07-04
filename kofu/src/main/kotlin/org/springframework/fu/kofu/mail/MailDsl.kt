/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.fu.kofu.mail

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.autoconfigure.mail.MailSenderPropertiesInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl
import org.springframework.fu.kofu.ApplicationDsl
import java.nio.charset.Charset

/**
 * Kofu DSL for Mail Sender configuration.
 *
 * Configure a [Mail Sender](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html).
 *
 * Required dependencies can be retrieve using `org.springframework.boot:spring-boot-starter-mail`.
 * @sample org.springframework.fu.kofu.samples.mailDsl
 * @author Ivan Skachkov
 */
class MailDsl(private val init: MailDsl.() -> Unit, private val mailProperties: MailProperties = MailProperties()) : AbstractDsl() {
    /**
     * SMTP server host. For instance, `smtp.example.com`.
     */
    var host: String
        get() = mailProperties.host
        set(value) {
            mailProperties.host = value
        }

    /**
     * SMTP server port.
     */
    var port: Int
        get() = mailProperties.port
        set(value) {
            mailProperties.port = value
        }

    /**
     * Login user of the SMTP server.
     */
    var username: String
        get() = mailProperties.username
        set(value) {
            mailProperties.username = value
        }

    /**
     * Login password of the SMTP server.
     */
    var password: String
        get() = mailProperties.password
        set(value) {
            mailProperties.password = value
        }

    /**
     * Protocol used by the SMTP server.
     */
    var protocol: String
        get() = mailProperties.protocol
        set(value) {
            mailProperties.protocol = value
        }

    /**
     * Default MimeMessage encoding.
     */
    var defaultEncoding: Charset
        get() = mailProperties.defaultEncoding
        set(value) {
            mailProperties.defaultEncoding = value
        }

    /**
     * Additional JavaMail Session properties.
     */
    val properties: MutableMap<String, String>
        get() = mailProperties.properties

    /**
     * Session JNDI name. When set, takes precedence over other Session settings.
     */
    var jndiName: String
        get() = mailProperties.jndiName
        set(value) {
            mailProperties.jndiName = value
        }

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        init()
        MailSenderPropertiesInitializer(mailProperties).initialize(context)
    }
}

/**
 * Configure a [Mail Sender](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html).
 *
 * Configuration from properties / environment (with "spring.mail" prefix) is a starting point for further customisation if needed.
 *
 * Require `org.springframework.boot:spring-boot-starter-mail` dependency.
 *
 * @sample org.springframework.fu.kofu.samples.mailDsl
 */
fun ApplicationDsl.mail(dsl: MailDsl.() -> Unit = {}) {
    val mailProperties = configurationProperties<MailProperties>(prefix = "spring.mail")
    MailDsl(dsl, mailProperties).initialize(context)
}
