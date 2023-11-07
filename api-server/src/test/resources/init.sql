create table agent (
                       agent_id bigint not null auto_increment,
                       is_delete boolean,
                       description varchar(255),
                       os varchar(255),
                       privateip varchar(255),
                       registered_at datetime(6),
                       primary key (agent_id)
) engine=InnoDB;

create table metric (
                        metric_id bigint not null auto_increment,
                        agent_id bigint,
                        cpu_rate float,
                        created_at datetime(6),
                        free_memory float,
                        inbound_traffic float,
                        outbound_traffic float,
                        total_memory float,
                        used_memory float,
                        primary key (metric_id)
) engine=InnoDB;
