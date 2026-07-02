CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    destination_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(500),
    created_at DATETIME NOT NULL
);