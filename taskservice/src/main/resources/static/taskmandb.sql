create database taskman;

use taskman;

create table task(
	task_id bigint primary key auto_increment,
    user_id bigint not null,
    title varchar(100),
    description varchar(500),
    status varchar(50),
    priority varchar(50),
    created_date date,
    due_date date
);

INSERT INTO task (user_id, title, description, status, priority, created_date, due_date) VALUES
(1, 'Complete project proposal', 'Write the initial draft for client approval', 'In progress', 'High', '2025-05-01', '2025-05-15'),
(1, 'Schedule team meeting', 'Coordinate with all departments for Q2 planning', 'Pending', 'Medium', '2025-05-10', '2025-05-20'),
(1, 'Organize workspace', 'Clean desk and setup new monitors', 'Completed', 'Low', '2025-04-25', '2025-04-30'),
(1, 'Review quarterly reports', 'Analyze sales data and prepare summary', 'In progress', 'High', '2025-05-05', '2025-05-18');

-- Tasks for User 6 (Mostly high priority)
INSERT INTO task (user_id, title, description, status, priority, created_date, due_date) VALUES
(6, 'Fix critical server bug', 'Emergency patch for database connection issues', 'In progress', 'High', '2025-05-12', '2025-05-13'),
(6, 'Update security certificates', 'Renew SSL certificates before expiration', 'Pending', 'High', '2025-05-15', '2025-05-17'),
(6, 'Backup database', 'Full system backup before migration', 'Completed', 'Medium', '2025-05-01', '2025-05-02'),
(6, 'Interview new candidates', 'Technical screening for developer position', 'In progress', 'Medium', '2025-05-08', '2025-05-22');

-- Tasks for User 9 (Personal tasks)
INSERT INTO task (user_id, title, description, status, priority, created_date, due_date) VALUES
(9, 'Book vacation flights', 'Research best prices for summer trip to Spain', 'Pending', 'Medium', '2025-05-10', '2025-06-01'),
(9, 'Renew gym membership', 'Annual renewal with personal trainer sessions', 'Completed', 'Low', '2025-04-28', '2025-05-05'),
(9, 'Prepare birthday party', 'Invite friends and order cake', 'In progress', 'Medium', '2025-05-12', '2025-05-25'),
(9, 'Learn React framework', 'Complete Udemy course on advanced React', 'Pending', 'Low', '2025-05-15', '2025-06-30');

-- Tasks for User 10 (Overdue tasks)
INSERT INTO task (user_id, title, description, status, priority, created_date, due_date) VALUES
(10, 'Submit tax documents', 'Federal and state tax filing', 'Pending', 'High', '2025-04-01', '2025-04-15'),
(10, 'Pay electricity bill', 'Last month''s utility payment', 'In progress', 'Medium', '2025-05-01', '2025-05-10'),
(10, 'Update resume', 'Add recent project experience', 'Pending', 'Low', '2025-05-05', '2025-05-30'),
(10, 'Schedule dentist appointment', 'Routine 6-month cleaning', 'In progress', 'Medium', '2025-05-08', '2025-05-28');

select * from task;

