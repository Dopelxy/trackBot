@echo off
title TrackBot

cd /d "%~dp0"

echo ====================================
echo          TrackBot starting...
echo ====================================
echo.

java -jar TrackBot.jar

echo.
echo ====================================
echo          TrackBot stopped.
echo ====================================
pause