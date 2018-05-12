# PaintApp
A java paint application

## Prerequisites:

* Java version "1.8.0_131" must be installed on your system to run the application
* JDK version "1.8.0_131" must be installed on your system if you want to compile and create executable

## Running executable
Double click on PaintApp.jar

## Instructions to compile and run PaintApp from command line

Run the following commands in "PaintApp" directory:

```
javac -cp src\Paint -d classes src\Paint\*.java
java -cp classes Paint.SketchApp
```

## Creating the executable
```
javac -cp src\Paint -d classes src\Paint\*.java
jar cfm PaintApp.jar Manifest.txt -C classes .
```