# 运行主游戏程序脚本
$classpath = "bin;resources;lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar;lib\lwjgl-3.3.2-natives-windows.jar;lib\lwjgl-glfw-3.3.2-natives-windows.jar;lib\lwjgl-opengl-3.3.2-natives-windows.jar;lib\lwjgl-openal-3.3.2-natives-windows.jar;lib\lwjgl-stb-3.3.2-natives-windows.jar"

Write-Host "Running JavaStg game..."
try {
    java -cp "$classpath" Main.Main
    Write-Host "Game completed!"
} catch {
    Write-Host "Game failed: $_"
    exit 1
}
