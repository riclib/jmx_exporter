mvn clean package -Dmaven.test.skip=true
cp jmx_prometheus_httpserver/target/jmx_prometheus_httpserver-0.11-SNAPSHOT-jar-with-dependencies.jar jmx_exporter_new.jar
