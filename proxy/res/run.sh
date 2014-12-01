#java -classpath "./conf/:./libs/*" Proxy
java -agentlib:jdwp=transport=dt_socket,server=n,address=localhost:5006,suspend=y  -classpath "./conf/:./libs/*" Proxy
