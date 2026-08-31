@echo off
chcp 65001 > nul
if not exist "bin\ec\edu\puce\test\LogiPackTest.class" (
    echo [!] No se encontro el codigo compilado. Compilando primero...
    call compilar.bat
)
java -Dfile.encoding=UTF-8 -cp bin ec.edu.puce.test.LogiPackTest
pause
