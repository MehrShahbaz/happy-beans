# Development Documentation
*Updated at 27 Aug 2025*

## Table of Contents
1. [General Principles](#general-principles)
2. [File and Directory Structure](#file-and-directory-structure)
3. [Branching Strategy](#branching-strategy)
4. [Naming Conventions](#naming-conventions)
5. [Code Review Guidelines](#code-review-guidelines)
6. [Development Workflow](#development-workflow)
7. [Commit Message Format](#commit-message-format)
8. [Pull Request Format](#pull-request-format)
9. [Documentation Standards](#documentation-standards)

## General Principles
- Follow the DRY (Don't Repeat Yourself) principle
- Keep functions small and focused on a single responsibility
- Use consistent formatting
- Comment TODOs, complex business logic and non-obvious implementations
- Follow SOLID principles and Spring Boot best practices
- Use LAZY fetching by default for JPA relationships; switch to EAGER only when always required
- Implement proper logging: controller logs for HTTP tracking, service logs for business logic
- Maintain comprehensive test coverage with Spring REST Docs integration

## File and Directory Structure
```
src/main/kotlin/happybeans/
├── config/                           # Spring configuration and filters
│   ├── advice/
│   │   └── GlobalExceptionHandler.kt # Global error handling
│   ├── argumentResolver/
│   │   ├── LoginMemberArgumentResolver.kt      # @LoginMember annotation resolver
│   │   └── RestaurantOwnerArgumentResolver.kt  # @RestaurantOwner annotation resolver
│   ├── interceptor/                  # Role-based HTTP interceptors
│   │   ├── AdminInterceptor.kt
│   │   ├── BaseAuthInterceptor.kt    # Common auth logic
│   │   ├── MemberInterceptor.kt
│   │   └── RestaurantOwnerInterceptor.kt
│   ├── CorrelationIdFilter.kt        # Request tracing
│   ├── DataInitializer.kt            # Database initialization
│   ├── MailConfig.kt                 # Email configuration
│   ├── StripeConfig.kt               # Payment configuration
│   └── WebConfig.kt                  # MVC and CORS configuration
├── controller/                       # REST API endpoints by user type
│   ├── admin/                        # Admin management endpoints
│   │   ├── AdminAuthController.kt
│   │   ├── AdminJoinRequestController.kt
│   │   ├── AdminRestaurantController.kt
│   │   ├── CreateAdminController.kt
│   │   └── CreateRestaurantOwnerController.kt
│   ├── dish/                         # Dish management (restaurant owner)
│   │   ├── DishController.kt
│   │   ├── DishOptionController.kt
│   │   └── DishSearchController.kt   # Filtered dish search for members
│   ├── guest/                        # Public endpoints (no auth required)
│   │   ├── GuestDishController.kt
│   │   └── GuestJoinRequestController.kt
│   ├── health/
│   │   └── HealthCheckController.kt  # System health endpoint
│   ├── member/                       # Member-specific endpoints
│   │   ├── CartProductController.kt
│   │   ├── MemberAuthController.kt
│   │   ├── MemberDishReviewController.kt
│   │   ├── MemberOrderController.kt
│   │   ├── MemberRestaurantReviewController.kt
│   │   └── MemberUserController.kt   # User preferences (likes/dislikes)
│   ├── payment/
│   │   └── WebhookController.kt      # Stripe payment webhooks
│   ├── restaurant/
│   │   └── RestaurantController.kt   # Restaurant management (restaurant owner)
│   ├── restaurantOwner/
│   │   └── RestaurantOwnerAuthController.kt
│   └── shared/                       # Shared system endpoints
│       └── TagController.kt          # Tag management
├── dto/                             # Data Transfer Objects by domain
│   ├── auth/                        # Authentication DTOs
│   │   ├── AuthTokenPayload.kt
│   │   └── LoginRequestDto.kt
│   ├── cart/                        # Shopping cart DTOs
│   ├── dish/                        # Dish and dish option DTOs
│   ├── error/                       # Error response DTOs
│   ├── joinRequest/                 # Restaurant owner application DTOs
│   ├── order/                       # Order and payment DTOs
│   ├── response/                    # Common response DTOs
│   ├── restaurant/                  # Restaurant management DTOs
│   ├── review/                      # Review system DTOs
│   ├── stripe/                      # Payment integration DTOs
│   ├── tag/                         # Tag system DTOs
│   └── user/                        # User management DTOs
├── enums/                           # Application-wide enumerations
│   ├── CreationSource.kt
│   ├── JoinRequestStatus.kt
│   ├── OrderStatus.kt
│   ├── PaymentOption.kt
│   ├── PaymentStatus.kt
│   ├── RestaurantStatus.kt
│   └── UserRole.kt
├── infrastructure/
│   └── JwtProvider.kt               # JWT token management
├── model/                           # JPA entities
│   ├── CartProduct.kt
│   ├── Dish.kt
│   ├── DishOption.kt
│   ├── DishReview.kt
│   ├── JoinRequest.kt               # Restaurant owner applications
│   ├── Order.kt
│   ├── OrderProduct.kt
│   ├── Payment.kt
│   ├── Restaurant.kt
│   ├── RestaurantReview.kt
│   ├── Tag.kt                       # Tag-based filtering system
│   ├── User.kt                      # Multi-role user entity
│   └── WorkingDateHour.kt
├── repository/                      # Data access layer with custom queries
│   ├── CartProductRepository.kt
│   ├── DishOptionRepository.kt      # Contains advanced filtering queries
│   ├── DishRepository.kt
│   ├── DishReviewRepository.kt
│   ├── JoinRequestRepository.kt
│   ├── OrderRepository.kt
│   ├── PaymentRepository.kt
│   ├── RestaurantRepository.kt
│   ├── RestaurantReviewRepository.kt
│   ├── TagRepository.kt
│   └── UserRepository.kt
├── service/                         # Business logic layer
│   ├── AdminAuthService.kt          # Admin authentication
│   ├── AdminRestaurantService.kt    # Admin restaurant management
│   ├── AdminUserService.kt          # Admin user management
│   ├── CartProductService.kt        # Shopping cart logic
│   ├── CreateRestaurantOwnerService.kt # Restaurant owner creation
│   ├── DishReviewService.kt         # Review system
│   ├── DishService.kt               # Dish management and filtering
│   ├── EmailDispatchService.kt      # Email notifications
│   ├── HandleRestaurantOwnerCreateService.kt # Owner creation workflow
│   ├── JoinRequestService.kt        # Restaurant owner applications
│   ├── LoginService.kt              # Generic login logic
│   ├── MemberAuthService.kt         # Member authentication
│   ├── MemberOrderService.kt        # Member order processing
│   ├── OrderPaymentService.kt       # Payment webhook handling
│   ├── PaymentService.kt            # Payment processing
│   ├── RestaurantOwnerAuthService.kt # Restaurant owner authentication
│   ├── RestaurantReviewService.kt   # Restaurant review system
│   ├── RestaurantService.kt         # Restaurant management
│   ├── StripePaymentService.kt      # Stripe integration
│   ├── TagService.kt                # Tag management with conflict resolution
│   └── UserService.kt               # User preferences management
├── utils/                           # Shared utilities
│   ├── annotations/
│   │   ├── LoginMember.kt           # Custom annotation for member injection
│   │   └── RestaurantOwner.kt       # Custom annotation for owner injection
│   ├── exception/                   # Custom business exceptions
│   │   ├── DishAlreadyExistsException.kt
│   │   ├── DuplicateEntityException.kt
│   │   ├── EntityNotFoundException.kt
│   │   ├── UnauthorisedUserException.kt
│   │   ├── UserAlreadyExistsException.kt
│   │   └── UserCredentialException.kt
│   └── mapper/
│       └── UserCreateRequestDtoMapper.kt
├── Application.kt                   # Spring Boot main class
└── resources/
    ├── application.properties       # Configuration properties
    └── schema.sql                   # PostgreSQL database schema

src/test/kotlin/happybeans/
├── TestFixture.kt                   # Shared test data and utilities
├── config/
│   ├── TestConfig.kt                # Test-specific configuration
│   ├── argumentResolver/
│   │   └── LoginMemberArgumentResolverTest.kt
│   └── interceptor/
│       ├── AdminInterceptorTest.kt
│       ├── MemberInterceptorTest.kt
│       └── RestaurantOwnerInterceptorTest.kt
├── controller/                      # API endpoint tests with REST Docs
│   ├── AbstractRestDocsMockMvcTest.kt      # Base class for REST Docs
│   ├── AbstractRestDocsRestAssuredTest.kt  # Alternative REST testing
│   ├── admin/                       # Admin controller tests
│   ├── dish/                        # Dish management tests
│   │   ├── DishControllerTest.kt
│   │   ├── DishE2ETest.kt
│   │   ├── DishOptionControllerTest.kt
│   │   └── DishSearchControllerTest.kt
│   ├── guest/                       # Public endpoint tests
│   ├── health/
│   │   └── HealthCheckControllerTest.kt
│   ├── member/                      # Member feature tests
│   └── restaurant/
│       └── RestaurantControllerTest.kt
├── dto/                             # DTO validation tests
├── infrastructure/
│   └── JwtProviderTest.kt           # JWT functionality tests
├── model/                           # Entity tests
├── repository/                      # Data access tests
│   ├── CartProductRepositoryTest.kt
│   ├── DishReviewRepositoryTest.kt
│   ├── RestaurantReviewRepositoryTest.kt
│   └── UserRepositoryTest.kt
└── service/                         # Business logic tests
    ├── AdminUserServiceTest.kt
    ├── CartProductServiceTest.kt
    ├── CreateRestaurantOwnerServiceTest.kt
    ├── DishReviewServiceTest.kt
    ├── DishSearchServiceTest.kt     # Tag filtering tests
    ├── DishServiceTest.kt
    ├── JoinRequestServiceTest.kt
    ├── LoginServiceTest.kt
    ├── MemberAuthServiceTest.kt
    ├── PaymentServiceTest.kt
    ├── RestaurantReviewServiceTest.kt
    ├── RestaurantServiceTest.kt
    └── UserServiceTest.kt
```
## Branching Strategy

### 1. Main Branches
- **`main`** – Production-ready code. Always deployable.  
  Only updated when the code is ready for release.
    - **Branch from:** `develop`

- **`develop`** – Integration branch for features; acts as the staging environment.
    - **Merge to:** `main`

- **`docs`** – For updating all documentation.
    - **Merge to:** `develop`

---

### 2. Feature Branches
- **Naming:** `feature/gitusername-short-description`
    - Example: `feature/codebrew42-user-authentication`
- **Purpose:** For new features and enhancements.
- **Branch from:** `develop`
- **Merge to:** `develop`

> Including member's GitHub username or `team` in the branch name helps indicate who is responsible for the feature.
---

### 3. Bugfix Branches
- **Naming:** `bugfix/gitusername-short-description`
    - Example: `bugfix/codebrew42-login-validation-error`
- **Purpose:** For fixing bugs in a specific feature branch.
- **Branch from:** corresponding **Feature Branch** or **develop**
- **Merge to:** the same **Feature Branch** or **develop**

---

### 4. Release Branches
- **Naming:** `release/version-number`
    - Example: `release/1.2.0`
- **Purpose:** For preparing new production releases.
- **Branch from:** `develop`
- **Merge to:** `main` and `develop`

## Naming Conventions

### API Endpoints
- Use kebab-case for endpoint paths
- Example: `/sign-up`, `/user-profile`, `/order-history`

```kotlin
@PostMapping("/sign-up")
@GetMapping("/user-profile")
@DeleteMapping("/order-history/{id}")
```

### Variables and Functions
- Use camelCase for variables and functions
- Use descriptive names that clearly indicate purpose
- Examples: `userId`, `calculateTotalPrice()`, `isUserAuthenticated`

### Classes and Interfaces
- Use PascalCase for class and interface names
- Examples: `UserService`, `OrderRepository`, `PaymentProcessor`

### Constants
- Use UPPER_SNAKE_CASE for constants
- Examples: `MAX_RETRY_ATTEMPTS`, `DEFAULT_TIMEOUT`, `API_BASE_URL`

### Database Tables and Columns
- Use snake_case for table and column names
- Examples: `user_profiles`, `order_items`, `created_at`

### Logging Conventions
- **Controller Logs**: HTTP method + description for request tracking
  - Example: `logger.info("GET Getting all restaurants for Admin")`
- **Service Logs**: Business action description using Kotlin string templates  
  - Example: `logger.info { "Creating tag: $tagName" }`
- **Log Levels**: Use appropriate levels (debug, info, warn, error)
  - `debug`: Internal framework operations, token details
  - `info`: Business actions, HTTP requests
  - `warn`: Business warnings, payment failures  
  - `error`: Exception conditions, system errors

## Code Review Guidelines
### Before Submitting
- [ ] Code follows established conventions
- [ ] Tests are written and passing
- [ ] README.md is updated
- [ ] No commented-out code
- [ ] No unnecessary files

### Review Checklist
- [ ] Code is readable and well-structured
- [ ] Logic is sound and efficient
- [ ] Edge cases are handled
- [ ] Security considerations addressed
- [ ] Performance implications considered
- [ ] Tests cover new functionality
- [ ] Documentation is accurate and complete
- [ ] Code is DRY

## Development Workflow

### Daily Workflow
#### Coding Workflow
1. Start from `develop` branch
2. Pull latest changes from `develop`
3. Create Feature Branch from `develop` branch
4. Start coding on the new feature branch

```bash
# 1. Start from `develop` branch
git switch develop

# 2. Pull latest changes from develop
git pull origin develop

# 3. Create Feature Branch from develop branch
git switch -c feature/new-feature-name

# 4. Start coding on the new feature branch
```

### How to Integrate Changes

#### Pre-Review Workflow
1. Commit and push changes to `Feature Branch`
2. Switch to `develop`, pull and merge `Feature Branch`
3. Push updates to `develop`

```bash
# 1. Commit and push changes to Feature Branch
git add .
git commit -m "Add new feature: description"
git push -u origin feature/new-feature-name  # Only first push requires `-u`

# 2. Switch to `develop`, pull and merge Feature Branch
git switch develop
git pull origin develop
git merge feature/new-feature-name

# 3. Push updates to `develop`
git push origin develop
```

### Review Process
1. Author creates **pull request** with clear description
2. At least **2 team members review** the code
3. Address all **feedback before approval**
4. (optional) **Squash commits** when merging to maintain clean history
    - To keep the main branch history clean before deployment
5. **Delete feature branch** after successful merge
    - Delete that feature branch on `GitHub`, not locally

## Commit Message Format

See `commit_template.md` in the project root directory

## Pull Request Format

See `pull_request_template.md` in the project root directory

## Architecture Patterns & Guidelines

### Authentication & Authorization
- **JWT-based Authentication**: All protected endpoints use JWT tokens
- **Role-based Access Control**: Admin, Restaurant Owner, Member roles with specific permissions
- **Custom Argument Resolvers**: `@LoginMember` and `@RestaurantOwner` annotations inject authenticated users
- **Interceptor-based Security**: Role-specific interceptors validate access before controller methods

### Tag System & Filtering
- **Bidirectional Tag Management**: Users can have likes/dislikes with automatic conflict resolution
- **Advanced Filtering**: Complex SQL queries in `DishOptionRepository` for personalized recommendations
- **Conflict Resolution Logic**: Adding to likes removes from dislikes (and vice versa)

### Data Access Patterns
- **Repository Pattern**: JPA repositories with custom query methods
- **Service Layer**: Business logic separated from controllers
- **DTO Pattern**: Data Transfer Objects for API contracts
- **Entity Relationships**: Proper JPA mappings with performance considerations

### Payment Integration
- **Stripe Webhooks**: Secure payment processing with signature verification
- **Order State Management**: PENDING → COMPLETED/REJECTED workflow
- **Email Notifications**: Automated confirmations and failure alerts

### Testing Strategy
- **Spring REST Docs**: API documentation generated from tests
- **Layered Testing**: Unit tests for services, integration tests for repositories
- **Test Fixtures**: Shared test data in `TestFixture.kt`
- **Mocking Strategy**: MockBean for external dependencies

## Development Commands

### Build and Test
```bash
./gradlew build                    # Build the application
./gradlew bootRun                  # Run the application (port 80)
./gradlew test                     # Run all tests
./gradlew ktlintCheck             # Check code formatting
./gradlew ktlintFormat            # Format code
./gradlew asciidoctor             # Generate API documentation
```

### Testing
```bash
./gradlew test                                    # Run all tests
./gradlew test --tests "*ControllerTest"         # Run controller tests only
./gradlew test --tests "*ServiceTest"            # Run service tests only
./gradlew test --tests "*RepositoryTest"         # Run repository tests only
```

### Documentation
```bash
./gradlew asciidoctor             # Generate Spring REST Docs
./gradlew copyDocs                # Copy docs to resources/static/docs
```

## Documentation Standards

### Code Documentation
- Document all public APIs
- Include parameter types and return values
- Provide usage examples for complex functions
- Keep comments up to date with code changes

### README Requirements
Project should include:
- Project description and purpose
- Installation instructions
- Usage examples
- API documentation
- Contributing guidelines

### API Documentation
- Spring REST Docs for automatic API documentation
- Include request/response examples in tests
- Document error codes and messages
- Keep documentation in sync with implementation
- Generated docs available at `/docs/index.html`

## Environment Setup

### Required Environment Variables
```bash
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password  
JWT_SECRET=your_jwt_secret_key
JWT_TIME=3600000
STRIPE_PUBLISHABLE_KEY=your_stripe_key
STRIPE_SECRET_KEY=your_stripe_secret
STRIPE_WEBHOOK_KEY=your_webhook_secret
```

### Database Setup
- PostgreSQL database required for production
- H2 in-memory database used for testing
- Database schema in `src/main/resources/schema.sql`
- Application runs on port 80 by default

*This document is a living resource and should be updated as our practices evolve. All team members are responsible for keeping it current and following these guidelines.*
