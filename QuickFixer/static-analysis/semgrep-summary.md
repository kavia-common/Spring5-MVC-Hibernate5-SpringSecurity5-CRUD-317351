# Static Analysis Report (Semgrep)

Project: QuickFixer (Spring 5 MVC + Hibernate + Spring Security)  
Date: 2026-02-07  
Tooling: semgrep (rules: `p/java`, `p/security-audit`, `p/owasp-top-ten`)

## High-priority findings

### 1) CSRF protection disabled (WARNING)
**File:** `src/main/java/com/umesh/myfixer/config/WebSecurityConfig.java`  
**What:** `csrf().disable()` disables CSRF protection globally.  
**Why it matters:** In session/cookie-auth apps, this increases risk of CSRF for state-changing endpoints.  
**Suggested fix:**
- Prefer enabling CSRF (default) and ensure JSP forms send CSRF tokens.
- If some endpoints are stateless APIs, disable CSRF only for those endpoints (selective ignore) instead of globally.

---

### 2) `@RequestMapping` without explicit HTTP method (WARNING, multiple occurrences)
**Files:**
- `src/main/java/com/umesh/myfixer/controller/MasterController.java`
- `src/main/java/com/umesh/myfixer/controller/TicketController.java`
- `src/main/java/com/umesh/myfixer/controller/UserController.java`

**What:** Several endpoints use `@RequestMapping(...)` without specifying `method=...`.  
**Why it matters:** Defaults allow multiple methods; if endpoints perform state changes, this can increase CSRF exposure and reduce clarity.  
**Suggested fix:**
- Use `@GetMapping` for views/list pages.
- Use `@PostMapping` for create/update operations.
- Use POST (or `@DeleteMapping`) for delete operations.

---

### 3) Potential DOM-based injection risk from `window.location.hash` selector usage (WARNING)
**File:** `src/main/webapp/resources/js/main.js`  
**What:** Uses `$(window.location.hash)` which is attacker-controlled.  
**Suggested fix:** Validate hash format (e.g. `^#[A-Za-z0-9_-]+$`) or use `document.getElementById(...)` safely.

## Additional maintainability notes (not from Semgrep rules, but observed)
- `UserController` uses `e.printStackTrace()`; replace with structured logging.
- `TicketController.getLoggedUser()` can NPE if user lookup fails; add null handling.
- `pom.xml` dependencies are old (Spring/Security/Hibernate/Gson/JUnit3); plan upgrades and add dependency vulnerability scanning.
- No Checkstyle/PMD/SpotBugs configured in Maven build; consider adding these for consistent quality gates.
