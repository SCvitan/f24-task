CREATE INDEX index_folders_parent_id ON folders (parent_id);

CREATE INDEX index_files_folder_id ON files (folder_id);