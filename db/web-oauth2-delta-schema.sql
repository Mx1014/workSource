-- eh_apps 加域空间id
ALTER TABLE eh_apps ADD COLUMN namespace_id INTEGER NOT NULL DEFAULT 0;