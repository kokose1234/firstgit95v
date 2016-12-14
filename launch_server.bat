@echo off
@title MoopleDEV Server v83
set CLASSPATH=.;dist\*
java -Dwzpath=wz\ net.server.Server
pause