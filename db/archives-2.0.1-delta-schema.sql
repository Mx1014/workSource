-- add by ryan at 03/01/02018.
ALTER TABLE eh_organization_member_details MODIFY employee_status TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal';

ALTER TABLE eh_archives_dismiss_employees MODIFY department VARCHAR(128) COMMENT '离职前部门';

ALTER TABLE eh_archives_dismiss_employees MODIFY employee_status TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN department_id BIGINT COMMENT '离职前部门id';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN job_position VARCHAR(128) COMMENT '离职前职位';

ALTER TABLE eh_archives_dismiss_employees ADD COLUMN job_level VARCHAR(128) COMMENT '离职前职级';

ALTER TABLE eh_archives_logs MODIFY operation_reason VARCHAR(1024) COMMENT 'the reason of the operation';

ALTER TABLE eh_archives_logs MODIFY operation_remark VARCHAR(1024) COMMENT 'the remark';
