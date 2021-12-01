CREATE database if NOT EXISTS `jwolf_travel` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `jwolf_travel`;
SET NAMES utf8mb4;
create table t_tourist_spots
(
	id bigint not null,
	name varchar(128) not null comment '名称',
	addr varchar(128) not null comment '地址',
	create_time datetime null,
	update_time datetime null,
	create_user bigint null,
	lat double(9,6) not null comment '纬度',
	lon double(9,6) not null comment '经度',
	deleted tinyint(2) default 0 not null,
	type tinyint(2) not null comment '景点类型',
	if_hot tinyint(2) null comment '是否热门',
	thumb_up bigint  not null comment '点赞数',
	city_code varchar(32) null comment '城市编码',
	price int default 0 not null comment '价格',
	constraint t_tourist_spots_pk
		primary key (id)
)
comment '旅游景点';


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

