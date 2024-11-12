This is part of migration project from monolithic app. Original repo you could find here:
https://github.com/szymek25/library_monolith

Main goal for the service is communication between front-end app and keycloak server.

## Quick description of the service:
This service is standalone application with own database. To minimize coupling with keycloak service there  is initialization mechanism introduced. 
To be able for work this app needs to have client api key set in USER_SERVICE_CLIENT_SECRET environment variable. During startup app checks InitState table in DB and if it is empty or latest entry has information that DB has to be initialized, then application fetches all user data from keycloak via REST API.
Once initialization is done, app listens for keycloak events on kafka topic and updates its database accordingly.
