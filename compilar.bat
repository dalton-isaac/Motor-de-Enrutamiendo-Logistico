@echo off
chcp 65001 > nul
echo ===================================================
echo   COMPILANDO PROYECTO LOGIPACK ECUADOR (PUCE TEC)
echo ===================================================
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src\ec\edu\puce\model\*.java src\ec\edu\puce\graph\*.java src\ec\edu\puce\dijkstra\*.java src\ec\edu\puce\view\*.java src\ec\edu\puce\test\*.java src\ec\edu\puce\Main.java
if %ERRORLEVEL% equ 0 (
    echo [OK] Compilacion completada exitosamente en la carpeta 'bin'.
) else (
    echo [ERROR] Ocurrio un error durante la compilacion.
)
pause
