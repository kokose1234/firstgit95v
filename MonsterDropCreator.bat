@echo off
set CLASSPATH=.;nbdist\*;
java -Dwzpath=wz\ tools.MonsterDropCreator false
pause