document.addEventListener('DOMContentLoaded', () => {
    // API details for nischalneupanee/hexplore
    const repoOwner = 'nischalneupanee';
    const repoName = 'hexplore';
    const apiUrl = `https://api.github.com/repos/${repoOwner}/${repoName}/releases/latest`;
    const fallbackDownloadUrl = `https://github.com/${repoOwner}/${repoName}/releases/latest/download/app-release.apk`;

    // DOM Elements
    const downloadBtn = document.getElementById('download-btn');
    const versionBadge = document.getElementById('version-badge');
    const sizeBadge = document.getElementById('size-badge');
    const dateBadge = document.getElementById('date-badge');
    const btnVersionText = document.getElementById('btn-version-text');
    const releaseTag = document.getElementById('release-tag');
    const releaseDate = document.getElementById('release-date');
    const releaseBody = document.getElementById('release-body');

    // Default Fallbacks
    const setFallbacks = () => {
        downloadBtn.href = fallbackDownloadUrl;
        versionBadge.innerHTML = `<i class="fa-solid fa-tag"></i> Version: Latest`;
        sizeBadge.innerHTML = `<i class="fa-solid fa-weight-hanging"></i> Size: ~18.5 MB`;
        dateBadge.innerHTML = `<i class="fa-solid fa-calendar-day"></i> Released: Dynamic`;
        btnVersionText.textContent = 'Click to download latest APK';
        
        releaseTag.textContent = 'Latest Stable Release';
        releaseDate.textContent = 'Always updated';
        releaseBody.innerHTML = `
            <p>Scan the QR code or click the download button to get the latest APK. The download always redirects to the most recent release.</p>
            <p><strong>Recent Improvements:</strong></p>
            <ul>
                <li>Complete database refactor with clean zone catalog assets.</li>
                <li>Updated cover photos for Main Gate, Registration Desk, Basketball Court, and Architecture Zone.</li>
                <li>Showcase list generated dynamically with offline database pre-population.</li>
                <li>Directional guidelines preserved for seamless navigation.</li>
            </ul>
        `;
    };

    // Helper: Format Date string (e.g. 2026-07-09T13:34:16Z -> July 9, 2026)
    const formatDate = (isoString) => {
        try {
            const date = new Date(isoString);
            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        } catch (e) {
            return isoString.substring(0, 10);
        }
    };

    // Helper: Convert markdown list items and bold tags to HTML
    const parseMarkdown = (markdownText) => {
        if (!markdownText) return 'No release description provided.';
        let html = markdownText
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/\r\n/g, '\n')
            .replace(/\n/g, '<br>');

        // Convert lists: e.g. - item
        html = html.replace(/(?:^|<br>)-\s+([^\n<]+)/g, '$1');
        // Convert bold: e.g. **text**
        html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
        // Convert code blocks: e.g. `code`
        html = html.replace(/`([^`]+)`/g, '<code>$1</code>');

        return html;
    };

    // Fetch latest release details from GitHub API
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`GitHub API error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const version = data.tag_name;
            const publishDate = formatDate(data.published_at);
            
            // Find APK asset
            const apkAsset = data.assets.find(asset => asset.name.endsWith('.apk'));
            
            if (apkAsset) {
                const downloadUrl = apkAsset.browser_download_url;
                const sizeMb = (apkAsset.size / (1024 * 1024)).toFixed(2);
                
                // Update primary download link
                downloadBtn.href = downloadUrl;
                btnVersionText.textContent = `Version ${version} • ${sizeMb} MB`;
                sizeBadge.innerHTML = `<i class="fa-solid fa-weight-hanging"></i> Size: ${sizeMb} MB`;
            } else {
                // If release exists but no APK asset found in it, fall back to permanent redirect
                downloadBtn.href = fallbackDownloadUrl;
                btnVersionText.textContent = `Version ${version}`;
                sizeBadge.innerHTML = `<i class="fa-solid fa-weight-hanging"></i> Size: ~18.5 MB`;
            }

            // Update badge info
            versionBadge.innerHTML = `<i class="fa-solid fa-tag"></i> Version: ${version}`;
            dateBadge.innerHTML = `<i class="fa-solid fa-calendar-day"></i> Released: ${publishDate}`;

            // Update release notes
            releaseTag.textContent = `Release ${version}`;
            releaseDate.textContent = `Published on ${publishDate}`;
            releaseBody.innerHTML = parseMarkdown(data.body);
        })
        .catch(error => {
            console.warn('Could not fetch release data from GitHub API:', error);
            // Apply fallbacks so the download link is still active and styled nicely
            setFallbacks();
        });
});
