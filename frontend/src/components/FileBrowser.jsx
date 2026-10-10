function FileBrowser({
    contents,
    breadcrumbs,
    loading,
    error,
    onCreateFolder,
    onCreateFile,
    onDeleteItem,
    onOpenFolder,
    onNavigate,
    onReturnToRoot,
  }) {
    return (
      <section>
        <h2>Folders and files</h2>
  
        <button onClick={onCreateFolder}>Create folder</button>{" "}
        <button onClick={onCreateFile} disabled={contents?.id == null}>
          Create file
        </button>
  
        <p>
          Location:{" "}
          {breadcrumbs.map((breadcrumb, index) => (
            <span key={`${breadcrumb.id}-${index}`}>
              {index > 0 && " / "}
              <button
                onClick={() => onNavigate(index)}
                disabled={loading}
              >
                {breadcrumb.name}
              </button>
            </span>
          ))}
        </p>
  
        {error && (
          <p role="alert">
            {error} <button onClick={onReturnToRoot}>Return to root</button>
          </p>
        )}
  
        {loading ? (
          <p>Loading...</p>
        ) : contents ? (
          <>
            <h3>Folders</h3>
  
            {contents.folders.length === 0 ? (
              <p>No folders.</p>
            ) : (
              <ul>
                {contents.folders.map((folder) => (
                  <li key={folder.id}>
                    <button onClick={() => onOpenFolder(folder)}>
                      {folder.name}
                    </button>{" "}
                    <button
                      onClick={() =>
                        onDeleteItem("folder", folder.id, folder.name)
                      }
                    >
                      Delete
                    </button>
                  </li>
                ))}
              </ul>
            )}
  
            <h3>Files</h3>
  
            {contents.files.length === 0 ? (
              <p>No files.</p>
            ) : (
              <ul>
                {contents.files.map((file) => (
                  <li key={file.id}>
                    {file.name}{" "}
                    <button
                      onClick={() =>
                        onDeleteItem("file", file.id, file.name)
                      }
                    >
                      Delete
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </>
        ) : null}
      </section>
    );
  }
  
  export default FileBrowser;