rootProject.name = "microservice-parent"

include("api-gateway","product-service", "order-service", "notification-service", "inventory-service", ":shared-schema")

