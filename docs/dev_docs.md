# Development Documentation

``` 
## Table of Contents
1. [File and Directory Structure](#code-conventions)
2. [Branching Strategy](#branching-strategy)
3. [Commit Message Format](#commit-message-format)
4. [Code Review Guidelines](#code-review-guidelines)
5. [Testing Standards](#testing-standards)
6. [Documentation Standards](#documentation-standards)
7. [Deployment Process](#deployment-process)
8. [Development Workflow](#development-workflow)
```

## Table of Contents
1. [File and Directory Structure](#file-and-directory-structure)
2. [Branching Strategy](#branching-strategy)
3. [Code Review Guidelines](#code-review-guidelines)



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
│   └── StripeClient.kt/
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
├── Applications.kt/
├── resources/
│   └── .../
└── test/
└──  └── .../
```


## Branching Strategy

### 1. Main Branches
- `main`: Production-ready code, always deployable, Never update it unless it's ready to be deployed
- `develop`: Integration branch for features, staging environment

### Supporting Branches
- Whenever we switch to our name branch, we need to merge with develop
```kotlin
//example: mariyakulikova

//1. start from `develop` branch        
git switch develop //If you are on another branch
git pull

//2. switch to `Name Branch`
git switch mariyakulikova
git merge develop

//3. switch to `Feature Branch`
git switch feature/*
git merge mariyakulikova
```
- After each feature is completed, that branch should be merged with this `Name Branches`
#### 2.`Name Branches`
- Example: origin/apoorva-s-natesh, origin/codebrew42, origin/mariyakulikova, origin/mehrshahbaz
    - Branch from: `develop`
    - Merge to: `devlop`

#### 3.`Feature Branches`
- feature/*: New features and enhancements
- Naming: feature/ticket-number-short-description
  - Example: feature/USER-123-user-authentication
  - Branch from: `Name Branches`
  - Merge to: `Name Branches`

#### 4.`Bugfix Branches`
- Naming: bugfix/ticket-number-short-description
  - Example: bugfix/BUG-456-login-validation-error
  - Branch from: `develop`
  - Merge to: `develop`

#### 5. `Realease Branches`
- release/*: Prepare new production releases
  - Naming: release/version-number
  - Example: release/1.2.0
  - Branch from: `develop`
  - Merge to: `main` and `develop`

## Code Review Guidelines
### Before Submitting
- [ ] Code follows established conventions
- [ ] Tests are written and passing
- [ ] Documentation is updated
- [ ] Self-review completed
- [ ] No console.log or debug statements
- [ ] No commented-out code

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
1. Pull latest changes from `develop`
2. Create feature branch from `Name Branch`

