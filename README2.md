# Sprint Framework - Git Guide

This guide explains how to use our Git repository to explore different stages of the Sprint Framework development.

## Branch Structure

The repository is organized into sprints, with each branch representing a complete sprint:

- `sprint0`: Basic FrontController setup
- `sprint1`: Controller discovery and listing
- `sprint2`: URL mapping to controllers
- `sprint3`: Controller instantiation (Singleton)
- `sprint4`: ModelView implementation
- `sprint5`: Error handling
- `sprint6`: Request parameter binding
- `sprint7`: Object parameter binding
- `sprint8`: Session management (latest version)

## How to Use

### 1. Getting Started

Clone the repository:
```bash
git clone https://github.com/ryannirina/sprint-framework.git
cd sprint-framework
```

### 2. Viewing Different Versions

To see how the framework looked at the end of a specific sprint:
```bash
git checkout sprint3  # Replace with sprint0-8
```

### 3. Learning Path

To learn the framework step by step:

1. Start with `sprint0`:
   ```bash
   git checkout sprint0
   ```
   This shows the basic FrontController setup.

2. Move to next sprint:
   ```bash
   git checkout sprint1
   ```
   And so on...

### 4. Features by Sprint

- **Sprint 0**: Basic servlet setup
  - FrontController
  - Request processing
  - JAR creation

- **Sprint 1**: Controller discovery
  - Find controllers using annotations
  - List available controllers
  - Configure package scanning

- **Sprint 2**: URL handling
  - Map URLs to controller methods
  - Handle relative URLs
  - Prevent duplicate mappings

- **Sprint 3**: Controller management
  - Singleton pattern
  - Method invocation
  - Return value handling

- **Sprint 4**: View handling
  - ModelView for data passing
  - Request attribute binding
  - View forwarding

- **Sprint 5**: Error handling
  - Package validation
  - URL validation
  - Return type checking

- **Sprint 6**: Parameter binding
  - Bind request parameters
  - Support custom parameter names
  - Type conversion

- **Sprint 7**: Object binding
  - Bind to Java objects
  - Field annotations
  - Nested parameters

- **Sprint 8**: Session handling
  - Custom session management
  - Session injection
  - Thread safety

## Example Usage

Here's a simple example of using the framework:

```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String sayHello(@RequestParam String name) {
        return "Hello, " + name;
    }

    @GetMapping("/greet")
    public ModelView greetWithView(@RequestParam String name) {
        ModelView mv = new ModelView();
        mv.setUrl("greeting.jsp");
        mv.addItem("message", "Hello, " + name);
        return mv;
    }
}
```

## Web.xml Configuration

```xml
<servlet>
    <servlet-name>FrontController</servlet-name>
    <servlet-class>com.sprintframework.web.servlet.FrontController</servlet-class>
    <init-param>
        <param-name>basePackage</param-name>
        <param-value>com.yourapp.controllers</param-value>
    </init-param>
</servlet>
```

## Tips

1. Each sprint branch contains working code
2. Check commit messages for detailed changes
3. Use the README in each branch for sprint-specific details
4. Main branch always has the latest version
