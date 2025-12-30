-- Create an ADMIN user
INSERT INTO members (email, password, name, address, member_role, created_at, updated_at, deleted_at)
VALUES ('admin@example.com', '{noop}1234', '관리자', '서울시 강남구', 'ADMIN', NOW(), NOW(), NULL);

-- Create a regular MEMBER user
INSERT INTO members (email, password, name, address, member_role, created_at, updated_at, deleted_at)
VALUES ('member@example.com', '{noop}1234', '일반사용자', '서울시 마포구', 'MEMBER', NOW(), NOW(), NULL);


-- Create initial boards
INSERT INTO boards (board_name, created_at, updated_at, deleted_at)
VALUES ('공지사항', NOW(), NOW(), NULL);

INSERT INTO boards (board_name, created_at, updated_at, deleted_at)
VALUES ('자유게시판', NOW(), NOW(), NULL);

INSERT INTO boards (board_name, created_at, updated_at, deleted_at)
VALUES ('질문게시판', NOW(), NOW(), NULL);