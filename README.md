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
- View template support
- Model and view integration
