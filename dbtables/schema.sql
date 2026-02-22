CREATE TABLE quiz (
  id serial PRIMARY KEY,
  title varchar(255),
  passage text,
  duration_minutes integer,
  published boolean default false,
  created_at timestamp default now()
);

CREATE TABLE question (
  id serial PRIMARY KEY,
  quiz_id integer REFERENCES quiz(id) ON DELETE CASCADE,
  text text,
  option_a varchar(1000),
  option_b varchar(1000),
  option_c varchar(1000),
  option_d varchar(1000),
  correct_index integer
);

CREATE TABLE result (
  id serial PRIMARY KEY,
  quiz_id integer,
  student_name varchar(255),
  score integer,
  total_questions integer,
  time_taken_seconds integer,
  submitted_at timestamp default now()
);
CREATE TABLE users (
  id serial PRIMARY KEY,
  name varchar(255) NOT NULL,
  email varchar(255) UNIQUE NOT NULL,
  password varchar(255) NOT NULL,
  role varchar(50) DEFAULT 'student', -- 'admin' or 'student'
  created_at timestamp DEFAULT now()
);
CREATE TABLE student_answers (
  id serial PRIMARY KEY,
  result_id integer REFERENCES result(id) ON DELETE CASCADE,
  question_id integer REFERENCES question(id) ON DELETE CASCADE,
  chosen_index integer,
  is_correct boolean
);
CREATE TABLE quiz_access (
  id serial PRIMARY KEY,
  quiz_id integer REFERENCES quiz(id) ON DELETE CASCADE,
  user_id integer REFERENCES users(id) ON DELETE CASCADE,
  allowed boolean DEFAULT true
);
