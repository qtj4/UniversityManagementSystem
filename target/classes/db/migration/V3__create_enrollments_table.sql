CREATE TABLE enrollments (
                             id BIGSERIAL PRIMARY KEY,
                             student_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             enrollment_date DATE,
                             grade VARCHAR(50),
                             FOREIGN KEY (student_id) REFERENCES users(id),
                             FOREIGN KEY (course_id) REFERENCES courses(id)
);