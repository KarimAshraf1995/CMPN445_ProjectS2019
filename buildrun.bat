mkdir build target
javac src/embedded/radar/RadarApp.java -sourcepath src -d target/
jar -cvfm build/RadarApp.jar manifest.mf -C target/ ./
java -jar build/RadarApp.jar