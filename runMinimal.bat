#mvn clean install -DskipTests=true
start cmd /c "title network-advisor & java -jar agents\network-advisor\target\demand-site-management-network-advisor-1.0-SNAPSHOT-jar-with-dependencies.jar"
echo Starting main container, click any button ONLY after container has started, check logs on another cmd window for details
pause
start cmd /c "title gateway & java -jar agents\gateway\target\gateway-agent-1.0-SNAPSHOT-jar-with-dependencies.jar"
start cmd /c "title customer & java -jar agents\customer-agent\target\customer-agent-1.0-SNAPSHOT-jar-with-dependencies.jar -container localhost -port 1099 -cid 123"
start cmd /c "title quoteManager & java -jar agents\quote-manager\target\demand-site-management-quote-manager-1.0-SNAPSHOT-jar-with-dependencies.jar"
start cmd /c "title customerHandler & java -jar agents\customer-handler\target\demand-site-management-customer-handler-1.0-SNAPSHOT-jar-with-dependencies.jar -configFile agents\customer-handler\customer_handler.properties"
start cmd /c "title trust & java -jar agents\trust-factor\target\trust-agent-1.0-SNAPSHOT-jar-with-dependencies.jar -configFile agents\trust-factor\trust_agent.properties"
