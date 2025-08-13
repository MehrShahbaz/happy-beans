# Development Documentation
*Updated at 13 Aug 2025* 

## Table of Contents
1. [General Principles](#general-principles)
2. [File and Directory Structure](#file-and-directory-structure)
3. [Naming Conventions](#naming-conventions)
4. [Branching Strategy](#branching-strategy)
5. [Code Review Guidelines](#code-review-guidelines)
6. [Development Workflow](#development-workflow)
7. [Commit Message Format](#commit-message-format)
8. [Pull Request Format](#pull-request-format)
9. [Documentation Standards](#documentation-standards)

## General Principles
- Write self-documenting code with clear, descriptive names
- Follow the DRY (Don't Repeat Yourself) principle
- Keep functions small and focused on a single responsibility
- Use consistent indentation and formatting
- Comment complex business logic and non-obvious implementations

## File and Directory Structure
```
project-root/
├── config/
│   ├── advice/
│   ├── argumentResolver/
│   └── intercepter/
├── controller/
│   ├── admin/
│   ├── member/
│   └── restaurantOwner/
├── dto/
│   └── .../
├── enums/
│   └── .../
├── infrastructure/
│   ├── ApplicationLogger.kt
│   ├── JwtProvider.kt
│   └── StripeClient.kt
├── model/
│   └── .../
├── repository/
│   └── .../
├── service/
│   └── .../
├── utils/
│   ├── annotations/
│   ├── exception/
│   ├── extensions/
│   └── helper/
├── Applications.kt
├── resources/
│   └── .../
└── test/
    └── .../
```

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

## Branching Strategy

### 1. Main Branches
- `main`: Production-ready code, always deployable, Never update it unless it's ready to be deployed
- `develop`: Integration branch for features, staging environment
- `docs` : branch for updating all the documentations

### 2. Feature Branches
- feature/*: New features and enhancements
- Naming: `feature/gitusername-short-description`
  - Example: `feature/codebrew42-user-authentication`
  - Branch from: `Name Branches`
  - Merge to: `Name Branches`

### 3. Bugfix Branches
- Naming: `bugfix/gitusername-short-description`
  - Example: `bugfix/codebrew42-login-validation-error`
  - Branch from: `develop`
  - Merge to: `develop`

### 4. Release Branches
- release/*: Prepare new production releases
  - Naming: `release/version-number`
  - Example: `release/1.2.0`
  - Branch from: `develop`
  - Merge to: `main` and `develop`

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

### Review Process
1. Author creates pull request with clear description
2. At least 2 team members review the code
3. Address all feedback before approval
4. Squash commits when merging to maintain clean history
5. Delete feature branch after successful merge

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

### Review Workflows
1. Author creates pull request with clear description
2. At least 2 team members review the code
3. Address all feedback before approval
4. (optional) Squash commits when merging to maintain clean history (only to `main` branch before deployment)
    - Squash commits : combining commits into one commit
    - To keep the main branch history clean and easy to navigate.
5. Delete feature branch after fixing bugs
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