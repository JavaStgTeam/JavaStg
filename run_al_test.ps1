# 运行OpenAL音频测试脚本
$classpath = "bin;lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar;lib\lwjgl-3.3.2-natives-windows.jar;lib\lwjgl-glfw-3.3.2-natives-windows.jar;lib\lwjgl-opengl-3.3.2-natives-windows.jar;lib\lwjgl-openal-3.3.2-natives-windows.jar;lib\lwjgl-stb-3.3.2-natives-windows.jar"

Write-Host "Running OpenAL audio test..."
try {
    java -cp "$classpath" Main.ALAudioTest
    Write-Host "Audio test completed successfully!"
} catch {
    Write-Host "Audio test failed: $_"
    exit 1
}
