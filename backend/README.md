# PLUGD Backend

1. copy `.env.sample` to `.env` and fill values.
2. create Postgres DB and run `db/init.sql`.
3. install deps: `npm install`
4. run: `npm run dev` (requires nodemon) or `npm start`

API:
- POST /api/auth/register {name, username, email, password}
- POST /api/auth/login {email, password}
- GET  /api/events
- POST /api/events {title, ...}