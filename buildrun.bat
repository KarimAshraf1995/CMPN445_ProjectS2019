mkdir build target
copy lib\*.jar build\
javac src/embedded/radar/RadarApp.java -sourcepath src -d target/ -cp "lib/jSerialComm-2.4.2.jar;."
jar -cvfm build/RadarApp.jar manifest.mf -C target/ ./ 
java -jar build/RadarApp.jar 