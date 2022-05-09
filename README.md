# shopping-list Project

You write your shopping list by hand? 😱 And you can't remember all ingredients of your receipts and are annoyed by adding all ingredients always to your shopping list?

Be happy! 🥳 This will do everything for your household 😉

You can also add receipts and just add the receipt and all ingredients will be added to your shopping list!!1

## Getting Started

Start the database docker container via

```shell script
./scripts/startPostgresContainer.sh
```

Then build the application initially

```shell script
./gradlew build
```

And finally start the application

```shell script
quarkus dev
```

You can also import the insomnia collection which is located in the scripts folder.

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/shopping-list-1.0-runner`
