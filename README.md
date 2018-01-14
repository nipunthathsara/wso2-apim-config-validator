# wso2-apim-config-validator
Validation tool for wso2 api manager configurations

validates main XML configuration files for WSO2 Api Manager 2.1.0
Validation is done against a JSON Knowledge base defined for each configuration file.

### Instructions
1. Build with mvn clean install and get the jar.
2. Run uploaddirectorycreator.sh in a desired directory.  
    sh uploaddirectorycreator.sh
3. Copy and paste conf directories in to their respective folder locations created.
4. Run the program with the argument given by the shell script (path to directory created)  
java -jar confvalidator-1.0-SNAPSHOT.jar ./upload
5. Results will be written in to error.log and log.out files.