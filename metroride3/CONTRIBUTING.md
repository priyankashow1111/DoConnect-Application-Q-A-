# Contributing to MetroRide API

## Branch Naming Rules

| Type       | Pattern                     | Example                        |
|------------|-----------------------------|--------------------------------|
| Feature    | `feature/<short-desc>`      | `feature/add-station-search`   |
| Bug Fix    | `fix/<short-desc>`          | `fix/null-timing-response`     |
| Hotfix     | `hotfix/<short-desc>`       | `hotfix/crash-on-empty-line`   |
| Release    | `release/<version>`         | `release/1.1.0`                |
| Chore      | `chore/<short-desc>`        | `chore/update-dependencies`    |

- Use lowercase and hyphens only (no underscores or spaces)
- Keep names short and descriptive

## Commit Message Format

Follow the **Conventional Commits** standard:

```
<type>(<scope>): <short summary>

[optional body]

[optional footer]
```

**Types:** `feat`, `fix`, `docs`, `chore`, `refactor`, `test`, `ci`

**Examples:**
```
feat(api): add GET /timings/{station} endpoint
fix(controller): handle missing station gracefully
docs(readme): add Gradle run instructions
chore(deps): bump spring-boot to 3.2.1
```

Rules:
- Summary in present tense, max 72 characters
- No period at the end
- Reference issue numbers in footer: `Closes #42`

## Merge Request Guidelines

1. Branch off `main` for features; keep branches short-lived
2. Ensure `mvn clean package` (or `gradle build`) passes locally before opening MR
3. MR title should match the main commit message format
4. Add a short description of what changed and why
5. Request at least **1 reviewer** before merging
6. Squash commits before merging to keep `main` history clean
7. Delete the branch after merge
