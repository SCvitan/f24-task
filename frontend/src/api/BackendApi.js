async function request(url, options = {}) {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
      },
      ...options,
    })
  
    if (!response.ok) {
      const message = await response.text()
      throw new Error(message || `Request failed: ${response.status}`)
    }
  
    if (response.status === 204) {
      return null
    }

    const responseText = await response.text()

    if (!responseText){
      return null
    }
  
    return JSON.parse(responseText)
  }
  
  export function getRootContents() {
    return request('/api/folders/root/contents')
  }
  
  export function getFolderContents(folderId) {
    return request(`/api/folders/${folderId}/contents`)
  }
  
  export function createFolder(name, parentId = null) {
    return request('/api/folders', {
      method: 'POST',
      body: JSON.stringify({ name, parentId }),
    })
  }
  
  export function createFile(name, folderId) {
    return request('/api/files', {
      method: 'POST',
      body: JSON.stringify({ name, folderId }),
    })
  }
  
  export function deleteFolder(folderId) {
    return request(`/api/folders/${folderId}`, {
      method: 'DELETE',
    })
  }
  
  export function deleteFile(fileId) {
    return request(`/api/files/${fileId}`, {
      method: 'DELETE',
    })
  }
  
  export function searchFiles(query, parentFolderId) {
    const params = new URLSearchParams({ query })
  
    if (parentFolderId != null) {
      params.set('parentFolderId', parentFolderId)
    }
  
    return request(`/api/search?${params.toString()}`)
  }

  export function searchFilesExact(query, parentFolderId) {
    const params = new URLSearchParams({ query })
  
    if (parentFolderId != null) {
      params.set('parentFolderId', parentFolderId)
    }
  
    return request(`/api/search/exact?${params.toString()}`)
  }