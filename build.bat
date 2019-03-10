mkdir dist target
javac src/embedded/radar/RadarApp.java -sourcepath src -d target/
jar -cvfm dist/RadarApp.jar manifest.mf -C target/ ./