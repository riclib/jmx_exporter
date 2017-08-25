mvn clean package -Dmaven.test.skip=true
copy jmx_prometheus_javaagent\target\jmx_prometheus_javaagent-0.11-SNAPSHOT-shaded.jar \coding\cftp\jmx_prometheus_javaagent-0.11.jar /Y
