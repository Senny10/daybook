# ADR-008: JWT Storage Strategy — localStorage

## Status
Accepted — 2026-05-05

## Context
The frontend needs to store the JWT token received from
POST /api/auth/login so it can be sent with subsequent API requests.
Two common approaches exist: localStorage and httpOnly cookies.

## Decision
We will store the JWT in localStorage under the key 'daybook_token'.

## Alternatives Considered

### httpOnly Cookie
- ✅ Cannot be read by JavaScript — immune to XSS attacks
- ✅ Automatically sent with every request by the browser
- ❌ Vulnerable to CSRF (Cross-Site Request Forgery) without
  additional CSRF token implementation
- ❌ Requires backend to set Set-Cookie header — our backend
  currently returns JWT in the response body, not as a cookie
- ❌ More complex CORS configuration required

### localStorage (chosen)
- ✅ Simple to implement — read/write with one line of code
- ✅ Works naturally with Axios request interceptors
- ✅ No backend changes required
- ✅ Immune to CSRF — cookies are not used
- ❌ Readable by any JavaScript on the page — vulnerable to XSS

## Consequences

### Easier
- Axios interceptor reads token from localStorage and adds
  Authorization header automatically — no per-request token handling
- Backend requires no changes — JWT is already returned in body
- Token expiry handled by response interceptor — 401 clears
  localStorage and redirects to login

### Harder
- XSS vulnerability is a real concern — mitigated by React's
  default HTML escaping, which prevents most XSS attack vectors
- Token persists across browser sessions until explicitly cleared
  or expired — logout must explicitly remove from localStorage

### Security Note
In a production financial system, httpOnly cookies with CSRF
protection would be the preferred approach. For Daybook as a
portfolio project, localStorage is an acceptable pragmatic choice,
documented here so the trade-off is explicit and defensible.