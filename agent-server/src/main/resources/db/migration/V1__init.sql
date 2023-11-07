CREATE TABLE METRIC (
                        metric_id     INTEGER PRIMARY KEY AUTOINCREMENT           NOT NULL,
                        total_memory     REAL           NOT NULL,
                        used_memory     REAL           NOT NULL,
                        free_memory     REAL           NOT NULL,
                        cpu_rate         REAL           NOT NULL,
                        inbound_traffic       REAL        NOT NULL,
                        outbound_traffic       REAL        NOT NULL,
                        created_at     TEXT        NOT NULL )
