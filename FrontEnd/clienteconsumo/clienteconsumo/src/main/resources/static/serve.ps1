$ErrorActionPreference = "Stop"
$port = $args[0]
if (-not $port) { $port = 8085 }
Write-Host "Serving static content on http://localhost:$port"
Start-Process python -ArgumentList "-m http.server $port" -WorkingDirectory $PSScriptRoot
