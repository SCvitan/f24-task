CREATE TABLE folders (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES folders(id) ON DELETE CASCADE
);

CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    folder_id BIGINT NOT NULL REFERENCES folders(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX folder_name_root
    ON folders (name)
    WHERE parent_id IS NULL;

CREATE UNIQUE INDEX folder_name_per_parent
    ON folders (parent_id, name)
    WHERE parent_id IS NOT NULL;

CREATE UNIQUE INDEX file_name_per_folder
    ON files (folder_id, name);