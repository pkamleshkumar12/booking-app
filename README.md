# booking-app

# Back-end Coding Assignment - B011
### Summary
Implement an ordering system to expose a Restful API to help AirAsia.com to receive a hotel
booking details and insert the details in the database.
### Task
- You should implement a Java-based CreatOrder API to receive a new order and insert it in the Database. In this API, we should be expecting to get below details in our API as a MINIMUM expectation:
-- Hotel ID
-- Hotel Name
-- Check-in Date
-- Check-out Date
-- Customer Details such as Name, Email and Number
-- Room details such as Room ID, Room Name and Number of guests
- Designing the database (SQL or NoSQL) as simple as possible while considering the future
growth of the application.
- Returning meaningful response code and details in the API response payload
- Please note down your assumptions when designing this solution
    
### Our Expectation
- Modular code structure
- Single responsibility principle
- Basic software design patterns
- Basic toolset usage ex. Lombok, Annotations, Stream API, etc.
- Test coverage
- Instruction to run the project
- Decent Git knowledge (Incremental commit history is preferred)
- Input and error validation

The possibilities are endless, you can do the absolute minimum to meet the above requirements
or go all out to build something amazing that implements testing or easy deployment or
awesome documentation. The stars are the limit to what you can build.

---
# Solution
### Assumption
1. `Hotel` will have limited types of `rooms`. And for the simplicity we will have the predefined rooms in DB. However there is endpoint to CRUD `Rooms` and `Hotels`
2. Rooms should have the attributes (other than in document), such as  
	1. `Types`: Premier, Premier Sea View, Signature Suite (limited types)
	2. `Capacity`: 2, 4, 6 (Max 6)
	3. `Number of Beds` (Max 6)
	4. `Amount` (price are not changing)
3. Rooms belong to `hotels`. Identified by Id in `rooms` Entity and Room ID is human readable attribute
4. For the simplicity, Order for the hotel booking should be either successful or failed. (No pending status or processing status)

### Approach
Following API-first approach, where I defined an API before start coding
Used [Stoplight studio](https://stoplight.io/) for API Design with `openapi: 3.0.0 spec`.
Used `openapi-generator-maven-plugin` to generate code from a Maven build

#### Project structure

 ```
 booking-app
├── app
│   └── pom.xml
│   └── src
│       └── main
│           └── java
│               └── com.airasia.booking
│                   └── BookingApp.java
├── specification
│   └── pom.xml
│   └── src
│       └── resources
│           └── bookings.v1.yml
└── pom.xml
 ```
