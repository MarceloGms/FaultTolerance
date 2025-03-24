compile:
	@mvn clean compile

run:
	@mvn exec:java -Dexec.mainClass=com.qcs.App