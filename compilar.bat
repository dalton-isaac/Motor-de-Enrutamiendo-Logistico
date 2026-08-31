@echo off
chcp 65001 > nul
echo ===================================================
echo   COMPILANDO PROYECTO LOGIPACK ECUADOR (JAVA)
echo ===================================================
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src\com\logipack\model\*.java src\com\logipack\graph\*.java src\com\logipack\dijkstra\*.java src\com\logipack\view\*.java src\com\logipack\test\*.java src\com\logipack\Main.java
if %ERRORLEVEL% equ 0 (
    echo [OK] Compilacion completada exitosamente en la carpeta 'bin'.
) else (
    echo [ERROR] Ocurrio un error durante la compilacion.
)
pause
