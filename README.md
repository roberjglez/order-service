# Order Service

The **Order Service** handles the creation and querying of orders within the microservices system.

## Features

- Create new orders.
- Retrieve existing order.

## Technologies Used

- **Java Spring Boot**: Main framework for building the service.
- **REST API**: Inter-service communication.
- **In-memory Database**: Utilizes a **HashMap** for storing orders during runtime. The HashMap is preloaded with some sample data for demonstration purposes.
- **Docker**: Service containerization.
- **Kubernetes**: Deployment orchestration.

## Endpoints

- `POST /orders`: Creates a new order.
- `GET /orders/{id}`: Retrieves an order by ID.

## Setup and Deployment

1. **Docker**:
    - Build the image:
      ```bash
      docker build -t order-service .
      ```
    - Run the container:
      ```bash
      docker run -p 8081:8081 order-service
      ```

2. **Kubernetes**:
    - Apply deployment and service manifests:
      ```bash
      kubectl apply -f deployment.yaml
      kubectl apply -f service.yaml
      ```

## Swagger Documentation

Swagger documentation is available at:

http://localhost:8081/swagger-ui.html