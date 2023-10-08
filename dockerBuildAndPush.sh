docker rmi post-service:latest
docker rmi vebstechbee03/tech-bee:post-service-latest
docker build -t post-service:latest .
docker tag post-service:latest vebstechbee03/tech-bee:post-service-latest
docker push vebstechbee03/tech-bee:post-service-latest