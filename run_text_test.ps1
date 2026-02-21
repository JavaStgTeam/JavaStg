# 运行文字渲染测试的PowerShell脚本

# 设置工作目录
Set-Location "e:\Myproject\Game\jstg_Team\JavaStg"

# 编译Java文件
Write-Host "编译Java文件..."
javac -cp ".;lib/*" -d "." src\Main\TextRenderTest.java src\stg\service\render\FontManager.java src\stg\service\render\TextRenderer.java src\stg\service\render\GLRenderer.java src\stg\service\render\ShaderProgram.java src\stg\service\render\VertexBuffer.java src\stg\util\ResourceManager.java

# 检查编译是否成功
if ($LASTEXITCODE -eq 0) {
    Write-Host "编译成功！"
    
    # 运行测试
    Write-Host "运行文字渲染测试..."
    java -cp ".;lib/*" Main.TextRenderTest
} else {
    Write-Host "编译失败！"
    exit 1
}