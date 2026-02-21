# 运行LWJGL测试脚本
$classpath = "bin;lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar;lib\lwjgl-3.3.2-natives-windows.jar;lib\lwjgl-glfw-3.3.2-natives-windows.jar;lib\lwjgl-opengl-3.3.2-natives-windows.jar;lib\lwjgl-openal-3.3.2-natives-windows.jar;lib\lwjgl-stb-3.3.2-natives-windows.jar"

Write-Host "Running LWJGL 3 test..."
try {
    java -cp "$classpath" Main.LWJGLTest
    Write-Host "Test completed successfully!"
} catch {
    Write-Host "Test failed: $_"
    exit 1
}