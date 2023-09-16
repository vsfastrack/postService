# postService
postService for tech-bee

To generate SQL changesets against changesets included in db.changesets-master.xml use the following command  
./gradlew generateSqlChangeSets -Dspring.profiles.active=[profile-name]

Copy the generated SQL changelogs and execute it on DB.