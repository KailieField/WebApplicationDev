plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm")

}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// SPRING BOOT
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	//FLYWAY / REST-ASSURED / JETBRAINS
	implementation("org.flywaydb:flyway-core")
	implementation("io.rest-assured:rest-assured")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// LOMBOK
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// DEV
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// POSTGRES RUN & TEST
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.testcontainers:postgresql")

	// MONGO RUN & TEST
	testImplementation("org.testcontainers:mongodb")

	// TEST
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")

}


tasks.withType<Test>{
	useJUnitPlatform()
}


