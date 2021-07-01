create table t_user (
    id int(12) not null auto_increment comment '编号',
    user_name varchar(16) not null comment '用户名',
    password varchar(16) not null comment '密码',
    primary key(id)
);