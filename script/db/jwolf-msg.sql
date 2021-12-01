create schema if not exists jwolf_msg collate utf8mb4_general_ci;
use jwolf_msg;
SET NAMES utf8mb4;
create table if not exists t_msg
(
	id bigint not null
		primary key,
	msg varchar(1024) not null comment '消息',
	type tinyint not null comment '消息类型',
	sender_id bigint not null comment '发送方ID',
	receiver_id bigint not null comment '接收方ID',
	create_time datetime not null,
	update_time datetime  null,
	deleted tinyint null,
	read_num int default 0 null comment '浏览次数'
)
comment '消息表';

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

