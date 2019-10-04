@echo off
echo -----------------------------------------------------------
echo Now, we would like to obtain UAC promotion.
echo -----------------------------------------------------------

fltmc>nul||cd/d %~dp0&&mshta vbscript:CreateObject("Shell.Application").ShellExecute("%~nx0","%1","","runas",1)(window.close)&&exit

cd /d %~dp0
echo -----------------------------------------------------------
echo Now, we would like to install the Java Runtime Environment.
echo -----------------------------------------------------------
start JDK\jdk-13_windows-x64_bin.exe
pause
echo -----------------------------------------------------------
echo Now, we start to set the environment.
echo -----------------------------------------------------------
xcopy Libs\x64\opencv_java411.dll "%ProgramFiles%\Java\jdk-13\bin"
echo -----------------------------------------------------------
echo All done, now we are ready to activate WuKong! Press A KEY
echo -----------------------------------------------------------
pause
start Jar\Main.jar
