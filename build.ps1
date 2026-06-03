# Build script: compile to bin directory

$srcDir = "src"
$binDir = "bin"

if (-not (Test-Path "$binDir")) {
    New-Item -ItemType Directory -Path "$binDir" -Force
    Write-Host "Create bin directory"
}

Write-Host "Cleaning bin directory..."
Remove-Item "$binDir\*" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "Finding Java files..."
$javaFiles = Get-ChildItem -Path "$srcDir" -Recurse -Filter "*.java"

if ($javaFiles.Count -eq 0) {
    Write-Host "No Java files found"
    exit 1
}

Write-Host "Compiling Java files to bin directory..."
try {
    $classpath = "lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar"
    javac -cp "$classpath" -d "$binDir" $javaFiles.FullName
    Write-Host "Compile success!"
} catch {
    Write-Host "Compile failed: $_"
    exit 1
}

Write-Host "Cleaning .class files in src directory..."
Get-ChildItem -Path "$srcDir" -Recurse -Filter "*.class" | Remove-Item -Force

Write-Host "Build completed! All compiled files are in bin directory."
