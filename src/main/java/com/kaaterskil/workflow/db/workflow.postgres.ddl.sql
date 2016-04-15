DO $$
DECLARE
 	seq_name 	varchar(50);
 	seq_names 	varchar[];
 	seq_count	integer;
BEGIN
  	seq_names = '{wf_def_deployments_id_seq, wf_run_tokens_id_seq, wf_run_variables_id_seq, wf_run_jobs_id_seq, wf_run_event_subscriptions_id_seq, wf_def_process_definitions_id_seq, wf_hist_process_instances_id_seq, wf_hist_activities_id_seq, wf_hist_comments_id_seq}';

  	FOR idx IN array_lower(seq_names, 1).. array_upper(seq_names, 1)
 	LOOP
  		seq_name := seq_names[idx];
  		
  		SELECT count(*)
          INTO seq_count
          FROM information_schema.sequences
         WHERE upper(sequence_name) = upper(seq_name);
    
    	IF seq_count = 0
  		THEN
   			EXECUTE 'create sequence ' || seq_name || ' increment by 1 no minvalue no maxvalue start with 1 cache 1';
  		END IF;
	END LOOP;
	
	CREATE TABLE IF NOT EXISTS wf_def_deployments (
  		deployment_id 	bigint NOT NULL DEFAULT nextval('wf_def_deployments_id_seq'::regclass),
  		name 			varchar(255),
  		category 		varchar(255),
  		deployed_at		timestamp(6) WITHOUT TIME ZONE,
  		CONSTRAINT wf_def_deployments_pk PRIMARY KEY (deployment_id)
  	);
 	CREATE INDEX wf_def_deployments_idx1 ON wf_def_deployments (name);
 	
 	CREATE TABLE IF NOT EXISTS wf_run_tokens (
  		token_id 					bigint NOT NULL DEFAULT nextval('wf_run_tokens_id_seq'::regclass),
 		parent_id 					bigint,
 		process_definition_id 		bigint,
 		root_process_instance_id	bigint,
 		process_instance_id 		bigint,
 		activity_id 				varchar(255),
 		name 						varchar(255),
 		is_active 					boolean,
 		is_scope 					boolean,
 		lock_time 					timestamp(6) WITHOUT TIME ZONE,
 		CONSTRAINT wf_run_tokens_pk PRIMARY KEY (token_id)
 	);
 	CREATE INDEX wf_run_tokens_idx1 ON wf_run_tokens (parent_id);
 	CREATE INDEX wf_run_tokens_idx2 ON wf_run_tokens (process_definition_id);
 	CREATE INDEX wf_run_tokens_idx3 ON wf_run_tokens (root_process_instance_id);
 	CREATE INDEX wf_run_tokens_idx4 ON wf_run_tokens (process_instance_id);
 	CREATE INDEX wf_run_tokens_idx5 ON wf_run_tokens (activity_id);
 	
 	CREATE TABLE IF NOT EXISTS wf_run_variables (
  		variable_id 			bigint NOT NULL DEFAULT nextval('wf_run_variables_id_seq'::regclass),
 		process_instance_id 	bigint,
 		token_id 				bigint,
 		name 					varchar(255) NOT NULL,
 		value_type 				varchar(100),
 		string_value 			varchar(4000),
 		long_value 				bigint,
 		int_value 				integer,
 		float_value				numeric(5),
 		boolean_value 			boolean,
 		created_at 				timestamp(6) WITHOUT TIME ZONE,
 		updated_at 				timestamp(6) WITHOUT TIME ZONE,
 		CONSTRAINT wf_run_variables_pk PRIMARY KEY (variable_id)
 	);
 	CREATE INDEX wf_run_variables_idx1 ON wf_run_variables (process_instance_id);
 	CREATE INDEX wf_run_variables_idx2 ON wf_run_variables (token_id);
 	CREATE INDEX wf_run_variables_idx3 ON wf_run_variables (name);

 	CREATE TABLE IF NOT EXISTS wf_run_jobs (
  		job_id 					bigint NOT NULL DEFAULT nextval('wf_run_jobs_id_seq'::regclass),
 		type 					varchar(30) NOT NULL,
 		process_definition_id 	bigint,
 		process_instance_id 	bigint,
 		token_id 				bigint,
 		retries 				integer,
 		due_by 					timestamp(6) WITHOUT TIME ZONE,
 		lock_expires_at 		timestamp(6) WITHOUT TIME ZONE,
 		lock_owner 				varchar(255),
 		is_exclusive 			boolean,
 		repeat                  varchar(255),
 		exception_message 		varchar(4000),
		exception_stack_trace	text,
 		CONSTRAINT wf_run_jobs_pk PRIMARY KEY (job_id)
 	);
 	CREATE INDEX wf_run_jobs_idx1 ON wf_run_jobs (type);
 	CREATE INDEX wf_run_jobs_idx2 ON wf_run_jobs (process_definition_id);
 	CREATE INDEX wf_run_jobs_idx3 ON wf_run_jobs (process_instance_id);
 	CREATE INDEX wf_run_jobs_idx4 ON wf_run_jobs (token_id);
 	
 	CREATE TABLE IF NOT EXISTS wf_run_event_subscriptions (
		event_subscription_id	bigint not null default nextval('wf_run_event_subscriptions_id_seq'::regclass),
		event_type				varchar(255),
		event_name				varchar(255),
		token_id				bigint,
		process_instance_id		bigint,
		process_definition_id	bigint,
		activity_id				varchar(255),
		configuration			varchar(255),
		created_at				timestamp (6) without time zone,
		version					integer,
		CONSTRAINT wf_run_event_subs_pk PRIMARY KEY (event_subscription_id)
	);
	CREATE INDEX wf_run_event_subs_idx1 ON wf_run_event_subscriptions (token_id);
	CREATE INDEX wf_run_event_subs_idx2 ON wf_run_event_subscriptions (configuration);
	
 	CREATE TABLE IF NOT EXISTS wf_def_process_definitions (
  		process_definition_id 	bigint NOT NULL DEFAULT nextval('wf_def_process_definitions_id_seq'::regclass),
 		key 					varchar(255) NOT NULL,
 		deployment_id 			bigint,
 		name 					varchar(255),
 		description 			varchar(4000),
 		category	 			varchar(255),
 		version 				integer NOT NULL DEFAULT 1,
 		CONSTRAINT wf_def_process_definitions_pk PRIMARY KEY (process_definition_id)
 	);
 	CREATE INDEX wf_def_process_definitions_idx1 ON wf_def_process_definitions (key);
 	CREATE INDEX wf_def_process_definitions_idx2 ON wf_def_process_definitions (deployment_id);
 	CREATE INDEX wf_def_process_definitions_idx3 ON wf_def_process_definitions (name);

 	CREATE TABLE IF NOT EXISTS wf_hist_process_instances (
  		process_instance_id 			bigint NOT NULL,
 		process_definition_id 			bigint NOT NULL,
 		runtime_process_instance_id 	bigint NOT NULL,
 		name 							varchar(255),
 		start_activity_id 				varchar(255),
 		end_activity_id 				varchar(255),
 		started_at 						timestamp(6) WITHOUT TIME ZONE,
 		ended_at 						timestamp(6) WITHOUT TIME ZONE,
 		duration 						integer,
 		CONSTRAINT wf_hist_process_instances_pk PRIMARY KEY (process_instance_id)
 	);
 	CREATE INDEX wf_hist_process_instances_idx1 ON wf_hist_process_instances (process_definition_id);
 	CREATE INDEX wf_hist_process_instances_idx2 ON wf_hist_process_instances (runtime_process_instance_id);
 	CREATE INDEX wf_hist_process_instances_idx3 ON wf_hist_process_instances (name);

 	CREATE TABLE IF NOT EXISTS wf_hist_activities (
		activity_id 			bigint NOT NULL DEFAULT nextval('wf_hist_activities_id_seq'::regclass),
 		type 					varchar(30) NOT NULL,
 		process_definition_id 	bigint NOT NULL,
 		process_instance_id 	bigint NOT NULL,
 		token_id 				bigint NOT NULL,
 		key 					varchar(255),
 		name 					varchar(255),
 		started_at 				timestamp(6) WITHOUT TIME ZONE,
 		ended_at 				timestamp(6) WITHOUT TIME ZONE,
 		duration 				integer,
 		CONSTRAINT wf_hist_activities_pk PRIMARY KEY (activity_id)
 	);
 	CREATE INDEX wf_hist_activities_idx1 ON wf_hist_activities (type);
 	CREATE INDEX wf_hist_activities_idx2 ON wf_hist_activities (process_definition_id);
 	CREATE INDEX wf_hist_activities_idx3 ON wf_hist_activities (process_instance_id);
 	CREATE INDEX wf_hist_activities_idx4 ON wf_hist_activities (token_id);
 	CREATE INDEX wf_hist_activities_idx5 ON wf_hist_activities (key);

 	CREATE TABLE IF NOT EXISTS wf_hist_comments (
  		comment_id 				bigint NOT NULL DEFAULT nextval('wf_hist_comments_id_seq'::regclass),
 		process_instance_id 	bigint,
 		activity_id 			bigint,
 		message 				varchar(4000),
 		created_at 				timestamp(6) WITHOUT TIME ZONE,
 		CONSTRAINT wf_hist_comments_pk PRIMARY KEY (comment_id)
 	);
 	CREATE INDEX wf_hist_comments_idx1 ON wf_hist_comments (process_instance_id);
 	CREATE INDEX wf_hist_comments_idx2 ON wf_hist_comments (activity_id);
 	
 	ALTER TABLE wf_def_process_definitions
 		ADD CONSTRAINT wf_def_process_definitions_uk1 UNIQUE (key, version);
 		
 	ALTER TABLE wf_run_tokens
 		ADD CONSTRAINT wf_run_tokens_fk1 FOREIGN KEY (process_instance_id) 
 		REFERENCES wf_run_tokens (token_id);
 		
 	ALTER TABLE wf_run_tokens
 		ADD CONSTRAINT wf_run_tokens_fk2 FOREIGN KEY (parent_id) 
 		REFERENCES wf_run_tokens (token_id);
 		
 	ALTER TABLE wf_run_tokens
 		ADD CONSTRAINT wf_run_tokens_fk3 FOREIGN KEY (process_definition_id) 
 		REFERENCES wf_def_process_definitions (process_definition_id);
 		
 	ALTER TABLE wf_run_variables
 		ADD CONSTRAINT wf_run_variables_fk1 FOREIGN KEY (token_id)
 		REFERENCES wf_run_tokens (token_id);
 		
 	ALTER TABLE wf_run_variables
 		ADD CONSTRAINT wf_run_variables_fk2 FOREIGN KEY (process_instance_id)
 		REFERENCES wf_run_tokens (token_id);
 		
	ALTER TABLE wf_run_event_subscriptions
		ADD CONSTRAINT wf_run_event_subs_fk1 FOREIGN KEY (token_id) 
		REFERENCES wf_run_tokens (token_id);
END
$$