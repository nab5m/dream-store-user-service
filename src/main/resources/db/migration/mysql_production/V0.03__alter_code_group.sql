-- UNIQUE 설정
ALTER TABLE code_group
    MODIFY COLUMN code_group_name VARCHAR(30) NOT NULL UNIQUE;