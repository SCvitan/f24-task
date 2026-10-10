import { useEffect, useState } from "react";
import {
  getRootContents,
  getFolderContents,
  createFolder,
  createFile,
  deleteFolder,
  deleteFile,
} from "./api/BackendApi";
import Search from "./components/Search";
import FileBrowser from "./components/FileBrowser";
import "./App.css";

function App() {
  const [contents, setContents] = useState(null);
  const [breadcrumbs, setBreadcrumbs] = useState([
    { id: null, name: "Root" },
  ]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function refreshContents(folderId = contents?.id ?? null) {
    const data =
      folderId === null
        ? await getRootContents()
        : await getFolderContents(folderId);

    setContents(data);
  }

  async function loadRoot() {
    setLoading(true);
    setError("");

    try {
      const data = await getRootContents();

      setContents(data);
      setBreadcrumbs([{ id: null, name: "Root" }]);
    } catch (err) {
      setError(err.message || "Failed to load root directory.");
    } finally {
      setLoading(false);
    }
  }

  async function openFolder(folder) {
    setLoading(true);
    setError("");

    try {
      const data = await getFolderContents(folder.id);

      setContents(data);
      setBreadcrumbs((current) => [
        ...current,
        { id: folder.id, name: folder.name },
      ]);
    } catch (err) {
      setError(err.message || "Failed to open folder.");
    } finally {
      setLoading(false);
    }
  }

  async function navigateToBreadcrumb(index) {
    if (index === breadcrumbs.length - 1) return;

    const target = breadcrumbs[index];

    setLoading(true);
    setError("");

    try {
      const data =
        target.id === null
          ? await getRootContents()
          : await getFolderContents(target.id);

      setContents(data);
      setBreadcrumbs((current) => current.slice(0, index + 1));
    } catch (err) {
      setError(err.message || "Failed to navigate to folder.");
    } finally {
      setLoading(false);
    }
  }

  async function handleCreateFolder() {
    const name = window.prompt("Enter folder name:");

    if (!name?.trim()) return;

    setError("");

    try {
      await createFolder(name.trim(), contents?.id ?? null);
      await refreshContents();
    } catch (err) {
      setError(err.message || "Failed to create folder.");
    }
  }

  async function handleCreateFile() {
    if (contents?.id == null) {
      setError("Files must be created inside a folder.");
      return;
    }

    const name = window.prompt("Enter file name:");

    if (!name?.trim()) return;

    setError("");

    try {
      await createFile(name.trim(), contents.id);
      await refreshContents();
    } catch (err) {
      setError(err.message || "Failed to create file.");
    }
  }

  async function handleDeleteItem(type, id, name) {
    if (!window.confirm(`Delete "${name}"?`)) return;

    setError("");

    try {
      if (type === "folder") {
        await deleteFolder(id);
      } else {
        await deleteFile(id);
      }

      await refreshContents();
    } catch (err) {
      setError(err.message || "Failed to delete item.");
    }
  }

  async function openSearchResultFolder(folderContents, folderPath) {
    setContents(folderContents);
    setBreadcrumbs([
      { id: null, name: "Root" },
      ...folderPath,
    ]);
    setError("");
  }

  useEffect(() => {
    loadRoot();
  }, []);

  return (
    <main>
      <h1>File Explorer</h1>

      <Search
        currentFolderId={contents?.id ?? null}
        onOpenFolder={openSearchResultFolder}
      />

      <hr />

      <FileBrowser
        contents={contents}
        breadcrumbs={breadcrumbs}
        loading={loading}
        error={error}
        onCreateFolder={handleCreateFolder}
        onCreateFile={handleCreateFile}
        onDeleteItem={handleDeleteItem}
        onOpenFolder={openFolder}
        onNavigate={navigateToBreadcrumb}
        onReturnToRoot={loadRoot}
      />
    </main>
  );
}

export default App;