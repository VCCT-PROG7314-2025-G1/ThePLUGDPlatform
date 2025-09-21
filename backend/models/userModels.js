const db = require("../db");

async function findByEmail(email) {
  const res = await db.query("SELECT * FROM users WHERE email=$1", [email]);
  return res.rows[0];
}

async function findByUsername(username) {
  const res = await db.query("SELECT * FROM users WHERE username=$1", [username]);
  return res.rows[0];
}

async function createUser({ name, username, email, password_hash }) {
  const res = await db.query(
    `INSERT INTO users (name, username, email, password_hash)
     VALUES ($1,$2,$3,$4) RETURNING *`,
    [name, username, email, password_hash]
  );
  return res.rows[0];
}

module.exports = {
  findByEmail,
  findByUsername,
  createUser
};