import { useEffect, useState } from "react";
import {
  getFolderContents,
  searchFiles,
  searchFilesExact,
} from "../api/BackendApi";

function Search({ currentFolderId, onOpenFolder }) {
  const [query, setQuery] = useState("");
  const [searchAllFiles, setSearchAllFiles] = useState(true);
  const [suggestions, setSuggestions] = useState([]);
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const searchQuery = query.trim();

    if (!searchQuery || (!searchAllFiles && currentFolderId == null)) {
      setSuggestions([]);
      return;
    }

    let cancelled = false;

    const timeoutId = setTimeout(async () => {
      try {
        const folderId = searchAllFiles ? null : currentFolderId;
        const files = await searchFiles(searchQuery, folderId);

        if (!cancelled) {
          setSuggestions(files);
          setError("");
        }
      } catch (err) {
        if (!cancelled) {
          setSuggestions([]);
          setError(err.message || "Failed to load suggestions.");
        }
      }
    }, 250);

    return () => {
      cancelled = true;
      clearTimeout(timeoutId);
    };
  }, [query, searchAllFiles, currentFolderId]);

  async function handleExactSearch(searchQuery = query) {
    if (!searchQuery.trim()) {
      setResults(null);
      setError("Enter a filename to search.");
      return;
    }

    if (!searchAllFiles && currentFolderId == null) {
      setResults(null);
      setError("Open a folder to search within it.");
      return;
    }

    setLoading(true);
    setError("");
    setSuggestions([]);

    try {
      const folderId = searchAllFiles ? null : currentFolderId;
      const files = await searchFilesExact(searchQuery.trim(), folderId);

      setResults(files);
    } catch (err) {
      setResults(null);
      setError(err.message || "Search failed.");
    } finally {
      setLoading(false);
    }
  }

  async function handleOpenContainingFolder(folderId) {
    setError("");

    try {
      const path = [];
      let folder = await getFolderContents(folderId);
      const targetContents = folder;

      while (folder) {
        path.push({
          id: folder.id,
          name: folder.name,
        });

        if (folder.parentId == null) break;

        folder = await getFolderContents(folder.parentId);
      }

      await onOpenFolder(targetContents, path.reverse());

      setResults(null);
      setSuggestions([]);
      setQuery("");
    } catch (err) {
      setError(err.message || "Failed to open containing folder.");
    }
  }

  return (
    <section>
      <h2>Search files</h2>

      <form
        onSubmit={(event) => {
          event.preventDefault();
          handleExactSearch();
        }}
      >
        <input
          value={query}
          onChange={(event) => {
            setQuery(event.target.value);
            setResults(null);
            setError("");
          }}
          placeholder="Search by filename"
          aria-label="Search by filename"
          autoComplete="off"
        />

        <button type="submit" disabled={loading}>
          {loading ? "Searching..." : "Search exact name"}
        </button>

        <label>
          <input
            type="checkbox"
            checked={searchAllFiles}
            onChange={(event) => {
              setSearchAllFiles(event.target.checked);
              setResults(null);
              setError("");
            }}
          />
          Search all folders
        </label>
      </form>

      {suggestions.length > 0 && results === null && (
        <div>
          <h3>Suggestions</h3>
          <ul>
            {suggestions.map((file) => (
              <li key={file.id}>
                <button
                  type="button"
                  onClick={() => {
                    setQuery(file.name);
                    handleExactSearch(file.name);
                  }}
                >
                  {file.name}
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}

      {error && <p role="alert">{error}</p>}

      {results !== null && (
        <div>
          <h3>Search results</h3>

          {results.length === 0 ? (
            <p>No matching files found.</p>
          ) : (
            <ul>
              {results.map((file) => (
                <li key={file.id}>
                  {file.name}{" "}
                  <button
                    type="button"
                    onClick={() =>
                      handleOpenContainingFolder(file.folderId)
                    }
                  >
                    Open folder
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </section>
  );
}

export default Search;