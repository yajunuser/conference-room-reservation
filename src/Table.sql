drop table meetroom;
create table meetroom(
id int primary key auto_increment,
meet_name VARCHAR(100) not null,
state boolean not null
);
alter table meetroom add column start_time datetime default null;
alter table meetroom add column end_time datetime default null
-- 事后添加的两个记录会议预定起始时间的列

DROP TABLE employee;
CREATE TABLE employee(
id int PRIMARY KEY auto_increment,
name varchar(20) not null,
password varchar(50) not null
);
insert into employee values (
null,'admin','admin'
),(
null,'李恒','123'
);


--创建一个记录预约的表格 记录预约者的名字 预约的开始时间 和预约的结束时间 用employee_id 标记预约者 用meet_room_id 表示预约的房间
--id  employee_id meet_room_id state(状态) start_time end_time create_time update_time
drop table meet_time;
create table meet_time(
id int primary key auto_increment,
employee_id int not null,
meet_room_id int  not null,
status int default value 1,
state  boolean not null,
start_time  datetime default null,
end_time  datetime default null,
create_time datetime default getTime(),
update_time datetime default null
);

/**
创建一个存储用户id 和uuid 的数据表格！
 */
 drop table employee_id_uuid if exists employee_id_uuid;
 create table employee_id_uuid{
 id int primary key auto_increment,
 employee_id int not null.
 uuid VARCHAR(200) not null
 };