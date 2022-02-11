# VirtuBois Editor

![VirtuBois screenshot](https://user-images.githubusercontent.com/32545895/57198755-79986300-6f44-11e9-87ca-74f1bad1140a.png)

## Setup

You will need the [JavaFX](https://openjfx.io/openjfx-docs/#install-javafx) external library installed and setup with IntelliJ.

## Executing

**Compiling**

```shell
mvn clean compile
```

**Packaging**

```shell
mvn clean install
```

**Executing jar**

```shell
java --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml -jar target/app-0.1.0.jar
```

## Team members

* Gabriel St-Pierre (me)
* Jordan Mayhue
* Martine Deschênes
* Yoan Chamberland
