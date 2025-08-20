# Commit Message Template Guide

## How to Set Up the Commit Message Template Locally

### Method 1: Project-Specific Template (Recommended)

1. **Create the commit template file**
```bash
# Navigate to your project root
cd /path/to/happy-beans

# Create the template file
touch .gitmessage
```

2. **Add content to the template**
```bash
# Edit the template file
nano .gitmessage
```
copy this [Template File Content](#template-file-content) to the .gitmessage

3. **Configure Git to use the template**
```bash
# Set the template for this project only
git config commit.template .gitmessage
```

4. **Add template to .gitignore (optional)**
```bash
echo ".gitmessage" >> .gitignore
```

### Method 2: Global Template
```bash
# Create global template
touch ~/.gitmessage

# Configure globally
git config --global commit.template ~/.gitmessage
```

## Commit Message Format

### Structure
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type List
- `feat` - Feature (a new feature)
- `fix` - Bug (bug fix)
- `refactor` - Refactor
- `design` - Changes to CSS or user UI design
- `comment` - Add or modify necessary comments
- `style` - Style (code formatting, adding semicolons: no change in business logic)
- `docs` - Documentation changes (add, modify, delete docs, README)
- `test` - Add, modify, delete test code
- `chore` - Other changes (e.g., modifying build scripts)
- `init` - Initial creation
- `rename` - Rename
- `remove` - Delete

### Guidelines
- **Title**: First letter capitalized, imperative mood, no period
- **Scope**: When only single file is updated, specify the file name
- **Body** (optional): Focus on what and why
- **Footer** (optional): Example: `Fixes: #47, Related to: #32`

### Examples
```
feat(memberController): Add user registration endpoint

Implement POST /sign-up with email validation and BCrypt password hashing.
Add input validation for email format and password strength requirements.

Closes #45
```

## Template File Content

Copy this content to your `.gitmessage` file:

```
# <type>(<scope>): <subject>
#
# <body>
#
# <footer>
# --- COMMIT END ---
#
#   Type List:
#   feat        : Feature (a new feature)
#   fix         : Bug (bug fix)
#   refactor    : Refactor
#   design      : Changes to CSS or user UI design
#   comment     : Add or modify necessary comments
#   style       : Style (code formatting, adding semicolons)
#   docs        : Documentation changes
#   test        : Add, modify, delete test code
#   chore       : Other changes (build scripts, etc.)
#   init        : Initial creation
#   rename      : Rename
#   remove      : Delete
#
#   Guidelines:
#   - Title: First letter capitalized, imperative mood, no period
#   - Scope: When only single file is updated, specify the file name
#   - Body: Focus on what and why (optional)
#   - Footer: Example: Fixes: #47, Related to: #32 (optional)
#
#   Example:
#   feat(auth): Add user registration endpoint
#
#   Implement user registration with email validation.
#   Includes input validation and error handling.
#
#   Closes #123
```
