# Inspector App

[![pablozoani](https://circleci.com/gh/pablozoani/ap-inspector.svg?style=svg)](https://app.circleci.com/pipelines/github/pablozoani/ap-inspector)

## Run without containers:

### Requirements:

-   Mongo DB
-   JDK 11+
-   Apache Maven
-   NodeJS

### Instructions:

```
git clone https://github.com/pablozoani/ap-inspector.git
```

```
cd ./ap-inspector
```

```
mongod --port 27017
```

```
mvn install
```

```
java -jar service-super-market/target/service-super-market-1.0-SNAPSHOT.jar
```

```
cd ./web-application
```

```
npm install && npm start
```

Visit http://localhost:3000 with your browser.

## Run with containers:

### Requirements:

-   Apache Maven
-   JDK 11+
-   Docker
-   Docker Compose

### Instructions:

```
git clone https://github.com/pablozoani/ap-inspector.git
```

```
cd ./ap-inspector
```

```
mvn install
```

```
docker-compose up
```

Visit http://localhost:3001 with your browser.

## About the project

This project is composed by:

-   The inspector module, which is my own alternative to the javax.validation package. This module is still work in progress, and complete test cases are provided. It is the main part of the project, and the rest of the modules were built to probe its functionality.
-   The super market module, which depends on the inspector module. It exposes a set of REST endpoints which are consumed by the web application. Also covers the following features:
    -   Searching.
    -   Sorting.
    -   Pagination.
    -   A controllers layer, tested.
    -   Another implementation of the controllers layers, but with the routing functions approach. Although this is not complete.
    -   Integration tests.
-   The web application module. Very simple but responsive and functional. It still needs a refactoring in the structure of the code, especially the code related to redux. At least it shows that the api's functionality works as expected.
