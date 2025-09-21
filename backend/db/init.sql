CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  username TEXT UNIQUE NOT NULL,
  email TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS events (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT,
  location TEXT,
  start_time TIMESTAMP,
  created_by INTEGER REFERENCES users(id),
  updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS offline_actions (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES users(id),
  payload JSONB,
  action_type TEXT,
  synced BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT now()
);