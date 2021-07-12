package org.springframework.fu.kofu.info

import org.springframework.boot.autoconfigure.info.ProjectInfoInitializer
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties
import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl
import org.springframework.fu.kofu.ConfigurationDsl

class ProjectInfoDsl(private val properties: ProjectInfoProperties, private val init: ProjectInfoDsl.() -> Unit) : AbstractDsl() {

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        init()
        ProjectInfoInitializer(properties).initialize(context)
    }
}

/**
 * Configure ProjectInfo support.
 * @see ProjectInfoDsl
 */
fun ConfigurationDsl.projectInfo(projectInfoDsl: ProjectInfoDsl.() -> Unit = {}) {
    ProjectInfoDsl(configurationProperties(prefix = "spring.info"), projectInfoDsl).initialize(context)
}