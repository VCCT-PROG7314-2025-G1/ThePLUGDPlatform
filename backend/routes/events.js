const express = require("express");
const db = require("../db");
const router = express.Router();

// GET /events
router.get("/", async (req, res) => {
  const result = await db.query("SELECT * FROM events ORDER BY start_time");
  res.json(result.rows);
});

// POST /events (protected in production)
router.post("/", async (req, res) => {
  const { title, description, location, start_time, created_by } = req.body;
  const result = await db.query(
    `INSERT INTO events (title, description, location, start_time, created_by)
     VALUES ($1,$2,$3,$4,$5) RETURNING *`,
    [title, description, location, start_time, created_by]
  );
  res.json(result.rows[0]);
});

module.exports = router;