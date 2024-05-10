insert into users(username, password) values ('TEST', '$2y$10$mr62OfqJlxMHVXeJT4fxHeKekh46gzkVYalnx9TYsCY5K3hPN5Ijy');
insert into user_role values (
                              (select id from users where username='TEST'),
                              'ADMIN'
                             ), ((select id from users where username='TEST'),
                              'STUDENT');