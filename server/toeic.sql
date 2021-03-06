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

create table [user]
(
	id int identity(1,1) primary key,
	name nvarchar(255) not null default '',
	gender bit default 0,
	birthday Date default CAST (GETDATE() as Date),
	address nvarchar(255) default '',
	email varchar(255) default '',
	score int default 0,
	account_id varchar(100),
	
	check (score <= 990 and score >= 0),
	
	constraint FK_User_Account foreign key (account_id)
	references account(id)
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

alter table [user]
add avatar int

insert into account values('EgihKqXOydeeD6b5VptzPbiLfSo2', 'zid.quocviet2000@gmail.com
 
', '', 'facebook')
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

select * from account
select * from dbo.[user]
select * from comment
select * from course
select * from category
select * from question

go

update [user] set avatar = 0
go

alter table account
add full_name nvarchar(255)
go

insert into account values('15b8b20c-29b7-11eb-adc1-0242ac120002', 'zid.quocviet2000@gmail.com', '12345678', default, N'Việt Mai Quốc')
insert into account values('15b8b48c-29b7-11eb-adc1-0242ac120002', '51800954@student.tdtu.edu.vn', '12345678', default, N'Trương Văn Long')
insert into account values('15b8b59a-29b7-11eb-adc1-0242ac120002', 'truongvana', '12345678', default, N'Nguyễn Văn A')
insert into account values('15b8b680-29b7-11eb-adc1-0242ac120002', 'truongvoky', '12345678', default, N'Trương Vô Kỵ')
insert into account values('15b8b75c-29b7-11eb-adc1-0242ac120002', 'doanchibinh', '12345678', default, N'Doãn Chí Bình')
insert into account values('15b8b9fa-29b7-11eb-adc1-0242ac120002', 'duongqua', '12345678', default, N'Dương Quá Underground')
insert into account values('15b8bad6-29b7-11eb-adc1-0242ac120002', 'tieulongnu', '12345678', default, N'Tiểu Long Nữ')
insert into account values('15b8bba8-29b7-11eb-adc1-0242ac120002', 'hongthatcong', '12345678', default, N'Hồng Thất Công')
insert into account values('f4ab31e7-41c9-4728-8e70-8bd5db25ca42', 'auduongphong', '12345678', default, N'Âu Dương Phong')
insert into account values('47d792fb-a81b-4c7c-a132-91b276b78339', 'hoangduocsu', '12345678', default, N'Hoàng Dược Sư')
go

insert into [user](name,account_id,score,avatar) values(N'Việt Mai Quốc', '15b8b20c-29b7-11eb-adc1-0242ac120002', 630, 0)
insert into [user](name,account_id,score,avatar) values(N'Trương Văn Long', '15b8b48c-29b7-11eb-adc1-0242ac120002', 550, 1)
insert into [user](name,account_id,score,avatar) values(N'Nguyễn Văn A', '15b8b59a-29b7-11eb-adc1-0242ac120002', 600, 2)
insert into [user](name,account_id,score,avatar) values(N'Trương Vô Kỵ', '15b8b680-29b7-11eb-adc1-0242ac120002', 730, 3)
insert into [user](name,account_id,score,avatar) values(N'Doãn Chí Bình', '15b8b75c-29b7-11eb-adc1-0242ac120002', 820, 4)
insert into [user](name,account_id,score,avatar) values(N'Dương Quá Underground', '15b8b9fa-29b7-11eb-adc1-0242ac120002', 655,3)
insert into [user](name,account_id,score,avatar) values(N'Tiểu Long Nữ', '15b8bad6-29b7-11eb-adc1-0242ac120002', 680, 2)
insert into [user](name,account_id,score,avatar) values(N'Hồng Thất Công', '15b8bba8-29b7-11eb-adc1-0242ac120002', 930,1)
insert into [user](name,account_id,score,avatar) values(N'Hoàng Dược Sư', '47d792fb-a81b-4c7c-a132-91b276b78339', 450,0)
insert into [user](name,account_id,score,avatar) values(N'Việt Mai Quốc', '15b8b20c-29b7-11eb-adc1-0242ac120002', 770, 4)
go

create table record
(
	id int identity(1,1),
	courseId int not null,
	accountId varchar(100) not null,
	score int,
	
	primary key(courseId, accountId),
	
	constraint FK_COURSE_RECORD foreign key (courseId)
	references course(id),
	constraint FK_ACCOUNT_RECORD foreign key (accountId)
	references account(id),
)
go

insert into record values(1, '15b8b75c-29b7-11eb-adc1-0242ac120002', 750)
insert into record values(1, '15b8b9fa-29b7-11eb-adc1-0242ac120002', 855)
insert into record values(1, '15b8bba8-29b7-11eb-adc1-0242ac120002', 725)
insert into record values(1, '15b8b48c-29b7-11eb-adc1-0242ac120002', 675)
insert into record values(1, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42', 525)

insert into record values(2, '15b8b75c-29b7-11eb-adc1-0242ac120002', 550)
insert into record values(2, '15b8b9fa-29b7-11eb-adc1-0242ac120002', 650)
insert into record values(2, '15b8bba8-29b7-11eb-adc1-0242ac120002', 780)
insert into record values(2, '15b8b48c-29b7-11eb-adc1-0242ac120002', 450)
insert into record values(2, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42', 500)
insert into record values(3, '15b8b75c-29b7-11eb-adc1-0242ac120002', 800)
insert into record values(3, '15b8b9fa-29b7-11eb-adc1-0242ac120002', 725)
insert into record values(3, '15b8bba8-29b7-11eb-adc1-0242ac120002', 675)
insert into record values(3, '15b8b48c-29b7-11eb-adc1-0242ac120002', 950)
insert into record values(4, '15b8b75c-29b7-11eb-adc1-0242ac120002', 990)
insert into record values(4, '15b8b9fa-29b7-11eb-adc1-0242ac120002', 705)
insert into record values(4, '15b8bba8-29b7-11eb-adc1-0242ac120002', 625)
insert into record values(4, '15b8b48c-29b7-11eb-adc1-0242ac120002', 550)
insert into record values(4, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42', 365)
insert into record values(5, '15b8b75c-29b7-11eb-adc1-0242ac120002', 685)
insert into record values(5, '15b8b9fa-29b7-11eb-adc1-0242ac120002', 505)
insert into record values(5, '15b8bba8-29b7-11eb-adc1-0242ac120002', 425)
insert into record values(5, '15b8b48c-29b7-11eb-adc1-0242ac120002', 500)
insert into record values(5, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42', 305)
go

select accountId, max(score) from record
group by accountId

select * from account
select * from record
select * from [user]

update [user] set score = 950 where account_id = '15b8b48c-29b7-11eb-adc1-0242ac120002'
update [user] set score = 990 where account_id = '15b8b75c-29b7-11eb-adc1-0242ac120002'
update [user] set score = 855 where account_id = '15b8b9fa-29b7-11eb-adc1-0242ac120002'
update [user] set score = 780 where account_id = '15b8bba8-29b7-11eb-adc1-0242ac120002'
update [user] set score = 525 where account_id = 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42'
go

insert into comment values(N'Hết khả năng rồi', 3, 2, '15b8b48c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Vừa đủ để ra trường TDTU rồi', 5, 2, '15b8b75c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Đề dễ mà được ít điểm quá', 5, 2, '15b8b9fa-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Tui được 780 điểm khá ổn', 4, 2, '15b8bba8-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Kiểu này tạch cmnr. Chán đéo buồn nói luôn', 1, 2, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42')


insert into comment values(N'Đề dễ quá dễ luôn', 3, 3, '15b8b48c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'800 điểm cũng ok', 5, 3, '15b8b75c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Học kiểu này cũng rất thích', 5, 3, '15b8b9fa-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Đề này chắc tui thấp điểm nhất rồi :((', 4, 3, '15b8bba8-29b7-11eb-adc1-0242ac120002')


insert into comment values(N'App này ai viết mà hay quá', 5, 1, '15b8b48c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Của Mai Quốc Việt với Trương Văn Long viết chứ ai', 5, 1, '15b8b75c-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Ủa hai bạn này học trường nào vậy?', 5, 1, '15b8b9fa-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'À TDTU á. MSSV là 51800954 vs 51800897', 5, 1, '15b8bba8-29b7-11eb-adc1-0242ac120002')
insert into comment values(N'Thầy dạy môn này là Mai Văn Mạnh đúng không?', 5, 1, 'f4ab31e7-41c9-4728-8e70-8bd5db25ca42')
go