const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const dotenv = require("dotenv");
dotenv.config();

const authRoutes = require("./routes/auth");
const eventsRoutes = require("./routes/events");

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Routes
app.use("/api/auth", authRoutes);
app.use("/api/events", eventsRoutes);

// Health check
app.get("/", (req, res) => res.send({ ok: true }));

const port = process.env.PORT || 4000;
app.listen(port, () => console.log(`Plugd API listening on ${port}`));