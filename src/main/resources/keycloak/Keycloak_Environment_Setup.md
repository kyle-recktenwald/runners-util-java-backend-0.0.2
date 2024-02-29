### Keycloak Environment Setup:
* Download the Keycloak server from [https://www.keycloak.org/downloads](https://www.keycloak.org/downloads)
* **To Start the Keycloak Server:**
```
cd ../keycloak-23.0.1/bin
./kc.sh start-dev --http-port 8180
```
* **Local Server URL:**
    * [http://localhost:8180](http://localhost:8180)
* **Local Admin Console URL:**
    * [http://localhost:8180/admin](http://localhost:8180/admin)
* Create an Initial Admin User
* Sign in to the admin console and create a realm using a ([resource file](./runnerutilsapp-realm.json))