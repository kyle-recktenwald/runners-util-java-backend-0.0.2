--Queries:
select * from users;
select * from routes;
select * from runs;

--Populate Test Users:
insert into users (user_id, create_date, update_date) values ('user_68883c2b-5ea3-4eee-a01a-09bfc0ff1ea2', '21-APR-2023', null);
insert into users (user_id, create_date, update_date) values ('user_6aa87957-06b9-4e39-8eb6-385fb1d95b8a', '21-APR-2023', null);

--Populate Test Routes:
insert into routes (route_id, create_date, distance, name, update_date, user_id) values ('aad2b007-e80b-41bb-8498-a2ddbd8a3bfc', '09-DEC-2022', 10621, 'Penn''s Landing - Race Street Pier Turnaround', null, 'b24849bc-f2c5-4d44-9527-95f2a25cc79c');
insert into routes (route_id, create_date, distance, name, update_date, user_id) values ('c3b7310a-db39-45e0-8652-a308b9e76f7f', '09-DEC-2022', 14484, 'Schuykill Banks - Lloyd Hall', null, 'b24849bc-f2c5-4d44-9527-95f2a25cc79c');

--Populate Test Runs:
insert into runs (run_id, create_date, date_time, distance, duration, update_date, route_id, user_id) VALUES ('86142f0a-9275-48f1-b403-a898a0768de8','08-DEC-2022', '08-DEC-2022', 10621,  3840000, null, 'aad2b007-e80b-41bb-8498-a2ddbd8a3bfc', 'b24849bc-f2c5-4d44-9527-95f2a25cc79c');
insert into runs (run_id, create_date, date_time, distance, duration, update_date, route_id, user_id) VALUES ('bbce7c08-3ff9-4096-aaff-de9ad1a503aa','09-DEC-2022', '09-DEC-2022', 10621,  3840000, null, 'aad2b007-e80b-41bb-8498-a2ddbd8a3bfc', 'b24849bc-f2c5-4d44-9527-95f2a25cc79c');
insert into runs (run_id, create_date, date_time, distance, duration, update_date, route_id, user_id) VALUES ('0b19078a-b77a-4322-b338-d05a8d924f8e','10-DEC-2022', '10-DEC-2022', 14484,  4800000, null, 'c3b7310a-db39-45e0-8652-a308b9e76f7f', 'b24849bc-f2c5-4d44-9527-95f2a25cc79c');
