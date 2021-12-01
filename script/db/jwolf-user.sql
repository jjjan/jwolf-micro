create schema if not exists jwolf_user collate utf8mb4_general_ci;
use jwolf_user;
SET NAMES utf8mb4;
create table if not exists t_user
(
	id bigint auto_increment comment '主键'
		primary key,
	username varchar(45) null comment '账号',
	password varchar(45) null comment '密码',
	nick_name varchar(20) null comment '昵称',
	avatar varchar(512) null comment '头像',
	email varchar(45) null comment '邮箱',
	phone varchar(11) null comment '手机',
	birthday date null comment '生日',
	sex tinyint null comment '性别',
	role_id bigint null comment '角色id',
	status tinyint null comment '状态',
	update_time timestamp null comment '修改时间',
	create_time timestamp null comment '创建时间',
	deleted tinyint default 0 null comment '是否已删除'
)
comment '用户表';

-- auto-generated definition
create table undo_log
(
    id            bigint auto_increment
        primary key,
    branch_id     bigint       not null,
    xid           varchar(100) not null,
    context       varchar(128) not null,
    rollback_info longblob     not null,
    log_status    int          not null,
    log_created   datetime     not null,
    log_modified  datetime     not null,
    constraint ux_undo_log
        unique (xid, branch_id)
)
    comment 'Seata分布式事务';



