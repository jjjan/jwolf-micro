create schema if not exists jwolf_msg collate utf8mb4_general_ci;

create table if not exists t_msg
(
	id bigint not null
		primary key,
	msg varchar(1024) not null comment '消息',
	type tinyint not null comment '消息类型',
	sender_id bigint not null comment '发送方ID',
	receiver_id bigint not null comment '接收方ID',
	create_time datetime not null,
	update_time datetime not null,
	deleted tinyint null,
	read_num int default 0 null comment '浏览次数'
)
comment '消息表';

