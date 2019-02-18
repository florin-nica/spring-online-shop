alter table customer
  add column user_id integer;
alter table customer
  drop column username;
alter table customer
  add constraint FK_user_customer foreign key (user_id) references users (id);

update customer
set user_id = 1
WHERE id = 1;
update customer
set user_id = 2
WHERE id = 2;