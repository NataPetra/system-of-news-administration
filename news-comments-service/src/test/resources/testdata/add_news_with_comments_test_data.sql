INSERT INTO "news-service-db".news (news_id, time, title, text) VALUES
    (1, TIMESTAMP '2023-11-03 01:46:22', 'News 1', 'This is a test news 1'),
    (2, TIMESTAMP '2023-11-03 01:46:22', 'News 2', 'This is a test news 2');

INSERT INTO "news-service-db".comments (comment_id, text, username, time, news_id)
VALUES
    (1, 'Comment 1 for News 1', 'subscriber', TIMESTAMP '2023-11-03 01:46:22', 1),
    (2, 'Comment 2 for News 1', 'subscriber2', TIMESTAMP '2023-11-02 21:00:24', 1);

INSERT INTO "news-service-db".comments (comment_id, text, username, time, news_id)
VALUES
    (3, 'Comment 1 for News 2', 'subscriber3', TIMESTAMP '2023-11-02 15:01:30', 2),
    (4, 'Comment 2 for News 2', 'subscriber4', TIMESTAMP '2023-11-01 12:40:55', 2);