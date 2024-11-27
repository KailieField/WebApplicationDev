pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.21"
    }
}
rootProject.name = "microservice-parent"

include("product-service", "order-service", "inventory-service")
