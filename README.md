# Renault Garage Project

## Description
This project is a microservice that manage garages, vehicles and their accessories for the brand Renault's.

---

## Technologies Used

### Technologies
- **Java 17** - Programming language
- **Spring Boot 3.5.9** - framework
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM implementation
- **H2 Database** - In-memory database
- **Mockito** - Mocking framework for unit tests
- **Spring Boot Test** - Integration testing support for controllers and services
- **RabbitMQ** - Message broker for event-driven communication

### Libraries
- **Lombok** - Reducing boilerplate code
- **MapStruct** - Object mapping between DTOs and entities

## Entity Model

### Entities and Relationships

#### Garage
- **Fields**: `garageId`, `garageName`, `address`, `phoneNumber`, `email`, `openingHours`
- **Relationships**:
  - One-to-Many with `Vehicle` (a garage can have multiple vehicles)
  - Embedded `Address`
  - Element collection of `GarageOpeningTime`

#### Vehicle
- **Fields**: `vehicleId`, `yearOfManufacture`, `fuelType`
- **Relationships**:
  - Many-to-One with `Garage` (each vehicle belongs to one garage)
  - Many-to-One with `Brand` (each vehicle has one brand)
  - Many-to-Many with `Accessory` (vehicles can have multiple accessories)

#### Brand
- **Fields**: `brandId`, `name`
- **Relationships**:
  - One-to-Many with `Vehicle` (a brand can have multiple vehicles)

#### Accessory
- **Fields**: `accessoryId`, `name`, `description`, `price`, `accessoryType`
- **Relationships**:
  - Many-to-Many with `Vehicle` (an accessory can belong to multiple vehicles)

#### Address *(Embeddable)*
- **Fields**: `country`, `city`, `zipCode`, `addressDetail`

#### GarageOpeningTime *(Embeddable)*
- **Fields**: `dayOfWeek`, `openingTime`
- **Embedded**: `OpeningTime`

#### OpeningTime *(Embeddable)*
- **Fields**: `startTime`, `endTime`
