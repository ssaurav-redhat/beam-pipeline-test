# beam-test-pipeline

### Prerequisite
1. Docker should be present
2. Java version should be 11
3. Maven should be installed



### Run this project

To run this project we need to first run docker containers for providing initial datasource using this command in the repo folder
```bash
docker-compose up
```

Once the container is up and running, connect to local myphpadmin by hitting http://localhost:8080. Once in there select exampledb and paste 
the contents of initial-data.sql to seed in the initial data required for running this pipeline.

Once data seeding is done, we will run the pipeline using the below command:
```bash
mvn compile exec:java -Dexec.mainClass=com.example.WordCount -Dexec.cleanupDaemonThreads=false -Pdirect-runner
```

Please note currently we are using direct-runner, as the project grows we can switch this to run on actual Spark runner.

Once the program is run successfully, the output files will be populated into this directory by the names
following this pattern `output.txt-00001-of-00002`
