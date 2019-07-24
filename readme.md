# Fantom Runtime

Core Fantom pods bundled as individual eclipse plugins.

Fantom Runtime is used internally by the [F4 IDE](https://github.com/xored/f4) to run plugins and compile Fantom code.

Use Maven to compile and package Fantom Runtime (tested with Maven 3.3.9):

  > mvn clean package
  
This should create an update directory in your project root named `org.fantom-updatesite`. To use this newly compiled version of Fantom Runtime with F4, edit the `pom.xml` file in the F4 project root. Find the `fantom-runtime` repository and update it to reference the new update site directory:

    <repository>
        <snapshots>
            <updatePolicy>always</updatePolicy>
        </snapshots>
        <id>fantom-runtime</id>
        <layout>p2</layout>
        <url>file:///D:/Projects/fantom-runtime/org.fantom-updatesite/target/site/</url>
    </repository>

Now you can use Maven to build F4 and include your new Fantom Runtime.
