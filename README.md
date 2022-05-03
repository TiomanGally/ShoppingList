# shopping-list Project

You write your shopping list by hand? 😱 And you can't remember all ingredients of your receipts and are annoyed by adding all ingredients always to your shopping list?

Be happy! 🥳 This will do everything for your household 😉

You can also add receipts and just add the receipt and all ingredients will be added to your shopping list!!1

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

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
