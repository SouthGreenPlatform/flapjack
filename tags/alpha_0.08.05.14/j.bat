@echo off

set lib=lib

set fjcp=%lib%\
set fjcp=%fjcp%;%lib%\castor-1.2-xml.jar
set fjcp=%fjcp%;%lib%\commons-logging-1.1.1.jar
set fjcp=%fjcp%;%lib%\i4jruntime.jar
set fjcp=%fjcp%;%lib%\mysql-connector-java-5.1.5-bin.jar
set fjcp=%fjcp%;%lib%\office-2.0.jar
set fjcp=%fjcp%;%lib%\scri-commons.jar
set fjcp=%fjcp%;%lib%\swing-layout-1.0.3.jar

java -Xmx768m -cp .;res;classes;%fjcp% flapjack.gui.Flapjack %1 %2 %3 %4 %5