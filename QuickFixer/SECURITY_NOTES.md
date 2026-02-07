# Security Notes (QuickFixer)

## CSRF policy
- **CSRF protection is enabled** (Spring Security default).
- CSRF tokens are required for all **state-changing** requests (e.g., POST to create/update/delete).
- We **do not disable CSRF globally**. If stateless API endpoints are introduced in the future, they may selectively opt out using `csrf().ignoringAntMatchers(...)` for those specific paths only.

## Controller mapping conventions
To make intent explicit and align with CSRF protection:
- Use **`@GetMapping`** for page renders and read-only operations (list/detail/form render).
- Use **`@PostMapping`** for create/update actions and for delete actions in classic JSP flows (HTML forms cannot easily submit `DELETE` without JavaScript).

Implemented examples:
- Ticket mutations:
  - `POST /webrst/saveTicketData`
  - `POST /webrst/deleteTicket`
- User mutations:
  - `POST /webrst/saveUserData`
  - `POST /webrst/deleteUser`

## JSP form requirements
- JSPs using Spring `<form:form>` automatically include the CSRF token.
- Any plain HTML `<form method="post">` must include a CSRF hidden input:
  - `<security:csrfInput />` (Spring Security JSP tag)

Updated pages:
- `WEB-INF/views/ticket.jsp` and `WEB-INF/views/user.jsp` now submit deletes via POST forms containing `<security:csrfInput />`.
- `WEB-INF/views/index.jsp` contact form includes `<security:csrfInput />`.
