const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const userModel = require("../models/userModel");
const dotenv = require("dotenv");
dotenv.config();

const JWT_SECRET = process.env.JWT_SECRET || "dev_secret";

async function register(req, res) {
  const { name, username, email, password } = req.body;
  if (!name || !username || !email || !password)
    return res.status(400).json({ error: "Missing fields" });

  const existing = await userModel.findByUsername(username) || await userModel.findByEmail(email);
  if (existing) return res.status(409).json({ error: "User already exists" });

  const saltRounds = 10;
  const password_hash = await bcrypt.hash(password, saltRounds);
  const user = await userModel.createUser({ name, username, email, password_hash });
  const token = jwt.sign({ sub: user.id, username: user.username }, JWT_SECRET, { expiresIn: "7d" });
  res.json({ token, user: { id: user.id, username: user.username, name: user.name, email: user.email } });
}

async function login(req, res) {
  const { email, password } = req.body;
  if (!email || !password) return res.status(400).json({ error: "Missing fields" });

  const user = await userModel.findByEmail(email);
  if (!user) return res.status(401).json({ error: "Invalid credentials" });

  const ok = await bcrypt.compare(password, user.password_hash);
  if (!ok) return res.status(401).json({ error: "Invalid credentials" });

  const token = jwt.sign({ sub: user.id, username: user.username }, JWT_SECRET, { expiresIn: "7d" });
  res.json({ token, user: { id: user.id, username: user.username, name: user.name, email: user.email } });
}

module.exports = { register, login };