package org.springframework.fu.kofu.samples

import org.springframework.boot.WebApplicationType
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.mail.mail

fun mailDsl() {
    application(WebApplicationType.REACTIVE) {
        mail {
            password = "secret_read_from_file"
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.ssl.enable"] = "true"
        }
    }
}
