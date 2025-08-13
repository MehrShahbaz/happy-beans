# Development Documentation


## Table of Contents
1. [General Principles](#general-principles)
2. [File and Directory Structure](#file-and-directory-structure)
3. [Branching Strategy](#branching-strategy)
4. [Code Review Guidelines](#code-review-guidelines)
5. [Development Workflow](#development-workflow)
6. [Commit Message Format](#commit-message-format)
7. [Documentation Standards](#documentation-standards)

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
- Whenever we switch to our name branch, we need to merge with `develop`
- After each feature is completed, that branch should be merged with this `Name Branches`
- 
#### 2.`Name Branches`
- Example: origin/apoorva-s-natesh, origin/codebrew42, origin/mariyakulikova, origin/mehrshahbaz
- Explanation: 
    - Branch from: `develop`
    - Merge to: `devlop`

#### 3.`Feature Branches`
- feature/*: New features and enhancements
- Naming: `feature/gitusername-short-description`
  - Example: `feature/codebrew42-user-authentication`
  - Branch from: `Name Branches`
  - Merge to: `Name Branches`

#### 4.`Bugfix Branches`
- Naming: `bugfix/gitusername-short-description`
  - Example: `bugfix/codewbrew42-login-validation-error`
  - Branch from: `develop`
  - Merge to: `develop`

#### 5. `Realease Branches`
- release/*: Prepare new production releases
  - Naming: `release/version-number`
  - Example: `release/1.2.0`
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

### Daily Workflow
#### Coding Workflow
1. Start from Name Branch (your personal branch)
2. Pull latest changes from develop into Name Branch 
3. Create Feature Branch from updated Name Branch 
4. Work on your feature

```bash
# Example: mariyakulikova

# 1. Switch to your Name Branch
git switch mariyakulikova

# 2. Pull latest changes from develop
git pull origin develop
# OR merge develop (but pull is cleaner)
git merge origin/develop

# 3. Create Feature Branch from Name Branch
git switch -c feature/new-feature-name
# This creates and switches to the new branch in one command

# Start coding on your feature branch
```

### How to Integrate Changes

#### Review Workflow
1. Commit and push changes to `Feature Branch`
2. Update `Feature Branch` with latest `Name Branch` changes
3. merge `Feature Branch` back to `Name Branch`
4. Push updated `Name Branch`
5. Create a pull request (on github)
6. Merge it to `develop`, once the pull request is approved
```bash
# 1. Commit and push changes to `Feature Branch`
git add .
git commit -m "Add new feature: description"
git push -u origin feature/new-feature-name  # First push sets upstream
# OR just: git push  # After upstream is set

#2. Update `Feature Branch` with latest `Name Branch` changes
git pull origin mariyakulikova

# 3. Switch to `Name Branch` and merge `Feature Branch`
git switch mariyakulikova
git merge feature/new-feature-name

# 4. Push updated Name Branch
git push origin mariyakulikova

# 5. Create a pull request (on github)
# 6. Merge it to `develop`, once the pull request is approved
git switch develop
git merge mariyakulikova


# 5. Clean up: Delete feature branch (optional)
git branch -d feature/new-feature-name
git push origin --delete feature/new-feature-name
```

### Communication
- Use project management tools for task tracking
- Daily standups for progress updates
- Document decisions in team wiki
- Notify team of breaking changes or major updates

## Commit Message Format

### Structure
```
<type>(<scope>): <subject>

<body>

<footer>
```


### <Type> list
- feat        : Feature (a new feature)
- fix         : Bug (bug fix)
- refactor    : Refactor
- design      : Changes to CSS or user UI design
- comment     : Add or modify necessary comments
- style       : Style (code formatting, adding semicolons: no change in business logic)
- docs        : Documentation changes (add, modify, delete docs, README)
- test        : Add, modify, delete test code
- chore       : Other changes (e.g., modifying build scripts)
- init        : Initial creation
- rename      : Rename
- remove      : Delete

### Title-Body-Footer
- Title: first letter capitalized, imperative mood, no period
- Body (optional)
    - focusing on what and why
    - If it's a batch commit affecting many files, add `Body`
- Footer (optional)
    - example: Fixes: #47, Related to: #32

### Examples
```
feat(auth): add user registration endpoint

Implement user registration with email validation and password hashing.
Includes input validation and error handling.

Closes #123
```

### Review Process
1. Author creates pull request with clear description
2. At least 2 team members review the code
3. Address all feedback before approval
4. Squash commits when merging to maintain clean history
5. Delete feature branch after successful merge

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