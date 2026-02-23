$ErrorActionPreference = "Stop"

Write-Host "清理bin目录..."
Remove-Item -Path "bin" -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path "bin" -Force | Out-Null

Write-Host "查找Java文件..."
$sourceFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse | Where-Object {
    $_.FullName -notmatch "src\\stg\\stage\\" -and
    $_.FullName -notmatch "src\\stg\\ui\\" -and
    $_.FullName -notmatch "src\\user\\enemy\\" -and
    $_.FullName -notmatch "src\\user\\bullet\\" -and
    $_.FullName -notmatch "src\\user\\spellcard\\" -and
    $_.FullName -notmatch "src\\user\\stage\\" -and
    $_.FullName -notmatch "src\\user\\stageGroup\\" -and
    $_.FullName -notmatch "src\\user\\boss\\" -and
    $_.FullName -notmatch "src\\stg\\base\\VirtualKeyboardPanel.java" -and
    $_.FullName -notmatch "src\\stg\\core\\CollisionSystem.java" -and
    $_.FullName -notmatch "src\\stg\\core\\GameWorld.java" -and
    $_.FullName -notmatch "src\\stg\\core\\IGameWorld.java" -and
    $_.FullName -notmatch "src\\stg\\event\\" -and
    $_.FullName -notmatch "src\\stg\\entity\\enemy\\Enemy.java" -and
    $_.FullName -notmatch "src\\stg\\entity\\enemy\\Boss.java" -and
    $_.FullName -notmatch "src\\stg\\entity\\enemy\\EnemySpellcard.java" -and
    $_.FullName -notmatch "src\\stg\\entity\\item\\Item.java" -and
    $_.FullName -notmatch "src\\stg\\entity\\laser\\Laser.java" -and
    $_.FullName -notmatch "src\\stg\\entity\\bullet\\Bullet.java" -or
    $_.FullName -match "src\\user\\player\\DefaultPlayer.java"
}

Write-Host "编译Java文件到bin目录..."
$libFiles = Get-ChildItem -Path "lib" -Filter "*.jar"
$libPath = ($libFiles | ForEach-Object { $_.FullName }) -join ";"

$sourceArgs = $sourceFiles | ForEach-Object { $_.FullName }

& javac -encoding UTF-8 -d bin -cp $libPath $sourceArgs 2>&1 | ForEach-Object {
    if ($_ -match "错误") {
        Write-Host $_ -ForegroundColor Red
    } else {
        Write-Host $_
    }
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "编译成功!"
} else {
    Write-Host "编译失败!"
    exit 1
}

Write-Host "清理src目录下的.class文件..."
Get-ChildItem -Path "src" -Filter "*.class" -Recurse | Remove-Item -Force

Write-Host "构建完成！"
