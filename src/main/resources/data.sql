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

INSERT INTO posts (
  title, content, like_count, view_count, member_id, board_id, created_at, updated_at, deleted_at
)
SELECT
  CONCAT(n, '번째 게시글') AS title,
  CONCAT(n, '번째 게시글 내용입니다.') AS content,
  MOD(n, 8) AS like_count,
  MOD(n * 3, 50) AS view_count,
  CASE WHEN MOD(n, 2) = 1 THEN 1 ELSE 2 END AS member_id,   -- 1~2 반복
  MOD(n - 1, 3) + 1 AS board_id,                             -- 1~3 반복
  NOW(), NOW(), NULL
FROM (
  SELECT (a.i * 10 + b.i) + 1 AS n
  FROM (SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
        UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
  CROSS JOIN
       (SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
        UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
) seq
WHERE n <= 100;
