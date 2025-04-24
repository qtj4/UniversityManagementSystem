CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         teacher_id BIGINT NOT NULL,
                         start_date DATE,
                         end_date DATE,
                         FOREIGN KEY (teacher_id) REFERENCES users(id)
);