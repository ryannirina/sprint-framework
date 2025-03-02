# Sprint Framework

A lightweight Java web framework inspired by Spring MVC, built incrementally through sprints. This framework demonstrates the core concepts of modern web application frameworks while maintaining simplicity and clarity.

## Project Structure
```
sprint-framework/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── sprintframework/
│                   └── web/
│                       └── servlet/
│                           └── FrontController.java
├── pom.xml
├── build.bat
└── README.md
```

## Development Progress

### Sprint 0: Front Controller ✅
**Purpose**: Set up the foundation with a Front Controller pattern  
**Components**:
- `FrontController` servlet as the entry point for all requests
- Basic request processing infrastructure
- Maven project setup with essential dependencies

**Technical Details**:
- Implements `javax.servlet.http.HttpServlet`
- Handles all HTTP methods through the `service()` method
- Basic URI extraction and response in `processRequest()`

### Upcoming Sprints

#### Sprint 1: Handler Mapping 🔄
**Purpose**: Route requests to appropriate handlers  
**Planned Components**:
- Handler mapping interface
- Default implementation of handler mapping
- Handler method resolution

#### Sprint 2: Controller Support
**Purpose**: Define and manage controllers  
**Planned Components**:
- Controller annotation support
- Method parameter resolution
- Request mapping infrastructure

#### Sprint 3: View Resolution
**Purpose**: Handle view rendering  
**Planned Components**:
- View resolver interface
- View template support
- Model and view integration

## Building the Project

1. Prerequisites:
   - Java 11 or higher
   - Maven 3.6 or higher

2. Build Command:
   ```bash
   ./build.bat
   ```
   This will create a JAR file in the `target` directory.

## Contributing

This is an educational project built incrementally through sprints. Each sprint adds new features while maintaining backward compatibility.

## License

This project is open source and available under the MIT License.
