@echo off
REM === УКАЖИТЕ ЗДЕСЬ СВОЙ ПУТЬ К JDK ===
set "JDK_PATH=C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot"

REM === Установка JAVA_HOME ===
setx JAVA_HOME "%JDK_PATH%" /M

REM === Добавление JAVA_HOME\bin в PATH ===
set "NEW_PATH=%JDK_PATH%\bin"
echo %PATH% | find /I "%NEW_PATH%" >nul
if errorlevel 1 (
    setx PATH "%PATH%;%NEW_PATH%" /M
    echo Добавлен %NEW_PATH% в PATH
) else (
    echo %NEW_PATH% уже есть в PATH
)

echo JAVA_HOME установлен на %JDK_PATH%
echo Перезапустите командную строку для применения изменений.
pause