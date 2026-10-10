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

      <div className="browser-toolbar">
        <p className="location">
          Location:{" "}
          {breadcrumbs.map((breadcrumb, index) => (
            <span key={`${breadcrumb.id}-${index}`}>
              {index > 0 && " / "}
              <button onClick={() => onNavigate(index)} disabled={loading}>
                {breadcrumb.name}
              </button>
            </span>
          ))}
        </p>

        <div className="toolbar-actions">
          <button onClick={onCreateFolder}>Create folder</button>
          <button onClick={onCreateFile} disabled={contents?.id == null}>
            Create file
          </button>
        </div>
      </div>

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
                <li className="file-row" key={folder.id}>
                <button
                  className="item-name"
                  onClick={() => onOpenFolder(folder)}
                >
                  {folder.name}
                </button>
              
                <button
                  className="delete-button"
                  onClick={() => onDeleteItem("folder", folder.id, folder.name)}
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
                <li className="file-row" key={file.id}>
                <span className="item-name">{file.name}</span>
              
                <button
                  className="delete-button"
                  onClick={() => onDeleteItem("file", file.id, file.name)}
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
