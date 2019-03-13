drop table if exists contacts;
create table contacts (
  id bigint auto_increment,
  firstName varchar(30) not null,
  lastName varchar(30) not null,
  phoneNumber varchar(11),
  emailAddress varchar(30)
);