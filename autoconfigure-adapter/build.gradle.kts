plugins {
	id("io.spring.dependency-management")
	id("java-library")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
	api("org.springframework.boot:spring-boot")
	api("org.springframework.boot:spring-boot-autoconfigure")

	compileOnly("org.springframework:spring-webmvc")
	compileOnly("javax.servlet:javax.servlet-api")
	compileOnly("org.springframework:spring-webflux")
	compileOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	compileOnly("org.springframework.data:spring-data-mongodb")
	compileOnly("org.mongodb:mongodb-driver-reactivestreams")
	compileOnly("org.springframework.data:spring-data-cassandra")
	compileOnly("org.springframework.data:spring-data-redis")
	compileOnly("redis.clients:jedis")
	compileOnly("io.lettuce:lettuce-core")
	compileOnly("com.fasterxml.jackson.core:jackson-databind")
	compileOnly("com.samskivert:jmustache")
	compileOnly("org.flywaydb:flyway-core")
	compileOnly("org.springframework.data:spring-data-r2dbc")
	compileOnly("io.r2dbc:r2dbc-postgresql")
	compileOnly("io.r2dbc:r2dbc-h2")
	compileOnly("io.r2dbc:r2dbc-mssql")
	compileOnly("com.github.jasync-sql:jasync-r2dbc-mysql:0.9.53")
}

repositories {
	maven("https://repo.spring.io/milestone")
}

publishing {
	publications {
		create(project.name, MavenPublication::class.java) {
			from(components["java"])
			artifactId = "spring-fu-autoconfigure-adapter"
			val sourcesJar by tasks.creating(Jar::class) {
				classifier = "sources"
				from(sourceSets["main"].allSource)
			}
			artifact(sourcesJar)
		}
	}
}
