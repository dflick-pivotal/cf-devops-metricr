# metricr quick start
- cd into "metricr"
````bash
cd metricr
````
- edit [credentials.json](credentials.json) and configure cloud foundry username and password.
````json
{
    "password" : "YOUR-PASSWORD",  
    "username" : "YOUR-USERNAME"
}
````
- create a credhub service instance
````bash
cf create-service credhub default credhub-metricr -c credentials.json
````
- create a mysql service instance
````bash
cf create-service p.mysql db-small db
````
- configure cloud foundry api endpoint in [manifest.yml](manifest.yml)
````yml
url: api.YOUR-CF-SYSTEM-DOMAIN
````
- cf push
````bash
cf push
````
# run the app on your local machine:
- export environment variables.  
````bash
export url=api.run.pivotal.io
export vcap_services_credhub_metricr_credentials_password=YOUR-PASSWORD
export vcap_services_credhub_metricr_credentials_username=YOUR-USERNAME
````
- run metricr application
````bash
java -Dreactor.ipc.netty.pool.acquireTimeout=1000000 -jar ./metricr/target/metricr-0.0.1-SNAPSHOT.jar
````
