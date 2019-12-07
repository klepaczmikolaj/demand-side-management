#mvn clean install -DskipTests=true
start cmd /c java -jar agents\network-advisor\target\demand-site-management-network-advisor-1.0-SNAPSHOT-jar-with-dependencies.jar
echo Starting main container, click any button ONLY after container has started, check logs on another cmd window for details
pause
start cmd /c java -jar agents\gateway\target\gateway-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
start cmd /c java -jar agents\customer-agent\target\customer-agent-1.0-SNAPSHOT-jar-with-dependencies.jar -container localhost -port 1099 -cid 1231
