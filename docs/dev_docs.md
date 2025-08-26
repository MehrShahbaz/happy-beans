# Development Documentation
*Updated at 15 Aug 2025*

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

## File and Directory Structure
```
project-root/
├── config/
│   ├── advice/
│   │   └── GlobalExceptionHandler.kt
│   ├── argumentResolver/
│   │   ├── LoginMemberArgumentResolver.kt
│   │   └── RestaurantOwnerArgumentResolver.kt
│   ├── interceptor/
│   │   ├── AdminInterceptor.kt
│   │   ├── BaseAuthInterceptor.kt
│   │   ├── MemberInterceptor.kt
│   │   └── RestaurantOwnerInterceptor.kt
│   ├── DataInitializer.kt
│   └── WebConfig.kt
├── controller/
│   ├── admin/
│   ├── dish/
│   │   └── DishController.kt
│   ├── guest/
│   ├── member/
│   │   ├── CartProductController.kt
│   │   ├── MemberAuthController.kt
│   │   ├── MemberDishReviewController.kt
│   │   ├── MemberOrderController.kt
│   │   └── MemberRestaurantReviewController.kt
│   ├── restaurant/
│   │   └── RestaurantController.kt
│   ├── restaurantOwner/
│   ├── review/
│   │   └── ReviewController.kt
│   └── tag/
│       └── TagController.kt
├── dto/
│   ├── auth/
│   ├── cart/
│   ├── dish/
│   ├── error/
│   ├── order/
│   ├── response/
│   ├── restaurant/
│   ├── review/
│   ├── tag/
│   └── user/
├── enums/
│   ├── CreationSource.kt
│   ├── OrderStatus.kt
│   ├── PaymentOption.kt
│   ├── PaymentStatus.kt
│   └── UserRole.kt
├── infrastructure/
│   └── JwtProvider.kt
├── model/
│   ├── CartProduct.kt
│   ├── Dish.kt
│   ├── DishOption.kt
│   ├── DishReview.kt
│   ├── Order.kt
│   ├── OrderProduct.kt
│   ├── Payment.kt
│   ├── Restaurant.kt
│   ├── RestaurantReview.kt
│   ├── Review.kt
│   ├── Tag.kt
│   ├── User.kt
│   └── WorkingDateHour.kt
├── repository/
│   ├── CartProductRepository.kt
│   ├── DishOptionRepository.kt
│   ├── DishRepository.kt
│   ├── DishReviewRepository.kt
│   ├── OrderRepository.kt
│   ├── PaymentRepository.kt
│   ├── RestaurantRepository.kt
│   ├── RestaurantReviewRepository.kt
│   ├── ReviewRepository.kt
│   ├── TagRepository.kt
│   └── UserRepository.kt
├── service/
│   ├── CartProductService.kt
│   ├── DishReviewService.kt
│   ├── DishService.kt
│   ├── LoginService.kt
│   ├── MemberAuthService.kt
│   ├── MemberOrderService.kt
│   ├── RestaurantReviewService.kt
│   ├── RestaurantService.kt
│   └── TagService.kt
├── utils/
│   ├── annotations/
│   │   ├── LoginMember.kt
│   │   └── RestaurantOwner.kt
│   ├── exception/
│   │   ├── DishAlreadyExistsException.kt
│   │   ├── DuplicateEntityException.kt
│   │   ├── EntityNotFoundException.kt
│   │   ├── UnauthorisedUserException.kt
│   │   ├── UserAlreadyExistsException.kt
│   │   └── UserCredentialException.kt
│   └── mapper/
│       └── UserCreateRequestDtoMapper.kt
├── Application.kt
├── resources/
│   └── application.properties
└── test/
    ├── TestFixture.kt
    ├── config/
    │   ├── argumentResolver/
    │   │   └── LoginMemberArgumentResolverTest.kt
    │   └── interceptor/
    │       ├── AdminInterceptorTest.kt
    │       ├── MemberInterceptorTest.kt
    │       └── RestaurantOwnerInterceptorTest.kt
    ├── controller/
    │   ├── dish/
    │   │   ├── DishControllerTest.kt
    │   │   └── DishE2ETest.kt
    │   ├── member/
    │   │   ├── AbstractDocumentTest.kt
    │   │   ├── CartProductControllerTest.kt
    │   │   ├── MemberAuthControllerTest.kt
    │   │   └── MemberDishReviewControllerTest.kt
    │   └── restaurant/
    │       └── RestaurantControllerTest.kt
    ├── dto/
    │   ├── auth/
    │   │   └── LoginRequestDtoTest.kt
    │   └── user/
    │       └── UserCreateRequestDtoTest.kt
    ├── infrastructure/
    │   └── JwtProviderTest.kt
    ├── model/
    │   ├── RestaurantTest.kt
    │   └── UserTest.kt
    ├── repository/
    │   ├── CartProductRepositoryTest.kt
    │   ├── DishReviewRepositoryTest.kt
    │   ├── RestaurantReviewRepositoryTest.kt
    │   └── UserRepositoryTest.kt
    └── service/
        ├── CartProductServiceTest.kt
        ├── DishReviewServiceTest.kt
        ├── DishServiceTest.kt
        ├── LoginServiceTest.kt
        ├── MemberAuthServiceTest.kt
        ├── RestaurantReviewServiceTest.kt
        └── RestaurantServiceTest.kt
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
- Use Swagger for REST APIs
- Include request/response examples
- Document error codes and messages
- Keep documentation in sync with implementation

*This document is a living resource and should be updated as our practices evolve. All team members are responsible for keeping it current and following these guidelines.*
