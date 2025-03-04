# Sprint Framework

A Java web framework inspired by Spring MVC.

## Sprints

### Sprint 0: Front Controller
- FrontController servlet
- processRequest method
- Jar creation script

### Sprint 1: Controller Discovery
- Controller annotation
- Package scanning for controllers
- List all controllers via /controllers endpoint
- Web.xml configuration for base package

### Sprint 2: URL Mapping
- GetMapping annotation for HTTP GET requests
- URL to controller method mapping
- Relative URL handling
- Duplicate URL detection
- 404 handling for unmapped URLs

### Sprint 3: View Resolution
- View resolver interface
- JSP view resolver implementation
- View template support
- Model and view integration

### Sprint 4: ModelView
- ModelView class with URL and data map
- Support for binding model attributes to request
- Request forwarding to views in /WEB-INF/views/
- Enhanced controller return handling

### Sprint 5: Error Handling
- Non-existent controller package detection
- Empty controller package validation
- Duplicate URL mapping prevention
- Detailed 404 error for unmapped URLs
- Return type validation and error reporting
- Proper HTTP status codes for different errors

### Sprint 6: Request Parameter Binding
- Automatic parameter name-based binding
- @RequestParam annotation for custom binding
- Support for required/optional parameters
- Type conversion for common Java types
- Detailed parameter binding error messages

### Sprint 7: Object Parameter Binding
- Added @RequestObject annotation for binding request parameters to objects
- Added @RequestField annotation for marking bindable object fields
- Support for nested parameter names with prefix
- Required field validation
- Mandatory parameter annotation requirement
