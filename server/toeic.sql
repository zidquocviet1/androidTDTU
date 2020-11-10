create database Toeic
go

use Toeic
go


create table account
(
	id varchar(100)	primary key,
	username varchar(100) collate latin1_general_cs_as not null,
	password varchar(100) collate latin1_general_cs_as not null,
	type varchar(20) not null default 'default'
)
go

create table course
(
	id int identity(1,1) primary key,
	name nvarchar(1000) not null,
	[description] nvarchar(1000),
	rating int default 0,
	
	check(rating <= 5 and rating >= 0),
)
go

create table category
(
	id int identity(1,1) primary key,
	name nvarchar(50),
	[description] nvarchar(1000)
)
go
create table question
(
	id int identity(1,1) primary key,
	[description] nvarchar(1000) not null,
	question_a nvarchar(1000) not null,
	question_b nvarchar(1000) not null,
	question_c nvarchar(1000) not null,
	question_d nvarchar(1000) not null,
	answer nvarchar(1000) not null,
	course_id int,
	category_id int,
	
	CONSTRAINT FK_course_question FOREIGN KEY (course_id)
	REFERENCES course(id),
	CONSTRAINT FK_category_question FOREIGN KEY (category_id)
	REFERENCES category(id)
)
go

create table comment
(
	id int identity(1,1) primary key,
	[description] nvarchar (1000) not null default '',
	rating int default 0,
	course_id int,
	[user_id] varchar(100),
	check(rating >= 0 and rating <= 5),
	
	CONSTRAINT FK_Account_Comment FOREIGN KEY ([user_id])
    REFERENCES account(id),
    CONSTRAINT FK_Course_Comment FOREIGN KEY (course_id)
    REFERENCES course(id)
)
go

insert into account values('EgihKqXOydeeD6b5VptzPbiLfSo2', 'zid.quocviet2000@gmail.com
 
', '', 'facebook')
go
insert into account values('KqXOydeeD6b5VptzPb', '51800954@student.tdtu.edu.vn
 
', '123456', default)
go

insert into course values ('Toeic 750', 'Khoa nay tap trung luyen thi 750 diem toeic', 4)
insert into course values ('Toeic 550', 'Khoa nay tap trung luyen thi 550 diem toeic', 3)
insert into course values ('Toeic 850', 'Khoa nay tap trung luyen thi 850 diem toeic', 3)
insert into course values ('Toeic 990', 'Khoa nay tap trung luyen thi 990 diem toeic', 5)
insert into course values ('Toeic 650', 'Khoa nay tap trung luyen thi 650 diem toeic', 3)
go

insert into category values('Statement', 'Chon ra loi tuyen bo dung cua cau hoi')
insert into category values('Choice', 'Chon ra cau dung')
insert into category values('Wh-question', 'Cau hoi WH')
insert into category values('Yes/no/tag - question', 'Cau hoi yes/no')
insert into category values('Word form', 'Cau hoi ve tu loai')
go

insert into question values('The assets of Marble Faun Publishing Company ___ last quarter when one of their main local distributors went out of business.'
, 'suffer', 'suffers', 'suffering', 'suffered', 'suffered', 1, 5)
insert into question values('lndie film director Luke Steele will be in London for the premiere of ___ new movie.'
, 'him', 'his', 'himself', 'he', 'his', 1, 5)
insert into question values('Laboratory employees are expected to wear a name tag and carry identification at ___ times.'
, 'full', 'complete', 'all', 'total', 'all', 1, 2)
insert into question values('The latest training ___ contains tips on teaching a second language to international students:'
, 'method', 'guide', 'staff', 'role', 'guide', 1, 2)
insert into question values('Once you have your resume with references and ___ , please submit it to the Human Resources Department on the 3rd floor.'
, 'qualified', 'qualifications', 'qualify', 'qualifying', 'qualifications', 1, 5)
go


insert into comment values('Khoa thi nay rat hay', 5,1, 'EgihKqXOydeeD6b5VptzPbiLfSo2')
insert into comment values('hay lam', 5,3, 'EgihKqXOydeeD6b5VptzPbiLfSo2')
insert into comment values('Rat ton nhung con nhieu thu chua co', 3,2, 'KqXOydeeD6b5VptzPb')
go

select * from account
select * from comment
select * from course
select * from category
select * from question

select * from account where password = 'quocviet'