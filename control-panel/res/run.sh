#java -classpath "./conf/:./libs/*" ControlPanel
java -agentlib:jdwp=transport=dt_socket,server=n,address=debian:5005,suspend=y -classpath "./conf/:./libs/*" ControlPanel