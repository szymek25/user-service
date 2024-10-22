docker remove library-user-service
docker image rm library-user-service
./gradlew build
docker build -t szymek25/library-user-service .
