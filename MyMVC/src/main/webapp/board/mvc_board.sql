drop table mvc_board;

create table mvc_board(
	num number(8) primary key, -- 글번호
	name varchar2(30) not null, -- 작성자
	passwd varchar2(20) not null, -- 글 비밀번호
	title varchar2(300) not null, -- 제목
	content varchar2(2000), -- 글 내용
	wdate date default sysdate, -- 작성일
	readnum number(8) default 0, -- 조회수
	fileName varchar2(500), -- 첨부파일
	fileSize number(8) -- 첨부파일 크기
	
);

drop sequence mvc_board_seq;

create sequence mvc_boadr_seq
start with 1
increment by 1
nocache;