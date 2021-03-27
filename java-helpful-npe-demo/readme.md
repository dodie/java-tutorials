# Java Helpful NullPointerExceptions Demo

This repository contains an example code that uses the DOM API to discover
an XML document.

A typical usage of this API can be seen in [line 83](https://github.com/dodie/java-14-helpful-npe-demo/blob/master/Main.java#L83):

```java
department.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
```

If any method returns a null in this chain, it will blow up with a `NullPointerException`.
But can you tell which?

**In the old days of Java all we got is**:

```
Exception in thread "main" java.lang.NullPointerException
        at Main.processEmployee(Main.java:83)
```

Finding out the exact source of the problem is left as an exercise for the reader.

However, **since Java 14 with [Helpful NullPointerExceptions](https://openjdk.java.net/jeps/358) the message contains a more
precise reason**:

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "org.w3c.dom.Node.getChildNodes()" because the return value of "org.w3c.dom.NodeList.item(int)" is null
        at Main.processEmployee(Main.java:83)
```

# Usage

## With Java 15 and above

```bash
# Compile
javac -source 15 Main.java

# Helpful NullPointerExceptions are enabled by default
java -cp . Main
```

## With Java 14

```bash
# Compile
javac --enable-preview -source 14 Main.java

# Run with Helpful NullPointerExceptions enabled
java -cp . --enable-preview -XX:+ShowCodeDetailsInExceptionMessages Main

# By default Helpful NullPointerExceptions are not enabled
java -cp . --enable-preview Main
```
