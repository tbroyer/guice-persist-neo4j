Using Neo4j with Guice Persist
==============================

[![Build Status](https://travis-ci.org/tbroyer/guice-persist-neo4j.png?branch=master)](https://travis-ci.org/tbroyer/guice-persist-neo4j)

_DISCLAIMER: this documentation heavily borrows from [Using JPA with Guice Persist](http://code.google.com/p/google-guice/wiki/JPA)._

How to Use
----------

To enable persistence support, simply install the Neo4j module:

```java
Injector injector = Guice.createInjector(..., Neo4jPersistModule.embedded("var/graphdb"));
```

There are various flavors, depending on the kind of database you need:

 * **Embedded database**: use `Neo4jPersistModule.embedded(storeDir)`, where `storeDir` has the same meaning as the argument to the [`EmbeddedGraphDatabase` constructor](http://components.neo4j.org/neo4j-kernel/milestone/apidocs/org/neo4j/kernel/EmbeddedGraphDatabase.html#EmbeddedGraphDatabase%28java.lang.String%29)

 * **Remote database**: `Neo4jPersistModule.remote(url)` and `Neo4jPersistModule.remove(url, username, password)` create a `RestGraphDatabase` using the [Java bindings for the Neo4j Server REST API](https://github.com/neo4j/java-rest-binding/) (which you'll have to add as a dependency)

 * **Test database**: `Neo4jPersistModule.impermanent()` will create an `ImpermanentGraphDatabase` (an embedded database that cleans the filesystem after itself), meant to be used in [unit tests](http://docs.neo4j.org/chunked/milestone/tutorials-java-unit-testing.html).

 * **Batch insertion**: use `Neo4jPersistModule.batch(storeDir)` to create [batch graph database](http://docs.neo4j.org/chunked/milestone/batchinsert.html#batchinsert-db) (this is only provided for convenience, you should generally use a `BatchInserter` instead)

Once installed, you'll have to start the persistence service (as with JPA or any other Guice Persist persistence provider), and only then you can inject a `GraphDatabaseService`. Also make sure you stop the persistence service to properly `shutdown()` the graph database service. In a web application, this is all taken care of by the `PersistFilter`.

A note on [transactions and units of work](http://code.google.com/p/google-guice/wiki/Transactions)
----------------------------------------

Transactions work the same as with Guice Persist for JPA: annotate a method with `@Transactional` and a transaction will be opened before and closed after it's called. Note however that Neo4j transactions are [_flat nested_](http://docs.neo4j.org/chunked/milestone/transactions-interaction.html).

Finally, Neo4j does not have the concept of sessions, so `UnitOfWork` is a no-op.

Maven dependency
----------------

```xml
<dependency>
  <groupId>net.ltgt.guice</groupId>
  <artifactId>guice-persist-neo4j</artifactId>
  <version>${guice-persist-neo4j.version}</version>
</dependency>
```

Snapshots are regularly (but manually) published to Sonatype OSS, so make sure you have the following repository configured in your POM or `settings.xml`, or proxied in your internal repository manager (note: the following snippet is borrowed from `org.sonatype.oss:oss-parent:pom:7`):

```xml
<repository>
  <id>sonatype-nexus-snapshots</id>
  <name>Sonatype Nexus Snapshots</name>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```

Licensing
---------

`guice-persist-neo4j` is covered by the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0). Note however that Neo4j is licensed under the terms of the GPLv3 (for Community editions), AGPLv3 (for Advanced and Enterprise editions), or a commercial license. See http://neo4j.org/licensing-guide/ for details.
