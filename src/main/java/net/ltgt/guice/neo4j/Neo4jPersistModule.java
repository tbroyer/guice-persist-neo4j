/*
 * Copyright 2012 Thomas Broyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ltgt.guice.neo4j;

import com.google.inject.Provides;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

import org.aopalliance.intercept.MethodInterceptor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder.DatabaseCreator;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSetting;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.util.Map;

public class Neo4jPersistModule extends PersistModule {

  public static Neo4jPersistModule batch(final String storeDir) {
    return new Neo4jPersistModule(new DatabaseCreator() {
      @Override
      public GraphDatabaseService newDatabase(Map<String, String> config) {
        return BatchInserters.batchDatabase(storeDir, config);
      }
    });
  }

  public static Neo4jPersistModule embedded(String path) {
    return new Neo4jPersistModule(new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(path));
  }

  public static Neo4jPersistModule impermanent() {
    return new Neo4jPersistModule(new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder());
  }

  public static Neo4jPersistModule remote(final String uri) {
    return new Neo4jPersistModule(new DatabaseCreator() {
      @Override
      public GraphDatabaseService newDatabase(Map<String, String> config) {
        return org.neo4j.rest.graphdb.GraphDatabaseFactory.databaseFor(uri);
      }
    });
  }

  public static Neo4jPersistModule remote(final String uri, final String username, final String password) {
    return new Neo4jPersistModule(new DatabaseCreator() {
      @Override
      public GraphDatabaseService newDatabase(Map<String, String> config) {
        return org.neo4j.rest.graphdb.GraphDatabaseFactory.databaseFor(uri, username, password);
      }
    });
  }

  private Neo4jLocalTnxInterceptor transactionInterceptor;

  private final GraphDatabaseBuilder builder;

  public Neo4jPersistModule(GraphDatabaseBuilder builder) {
    this.builder = builder;
  }

  public Neo4jPersistModule(DatabaseCreator creator) {
    this(new GraphDatabaseBuilder(creator));
  }

  public Neo4jPersistModule settings(Map<GraphDatabaseSetting, String> settings) {
    for (Map.Entry<GraphDatabaseSetting, String> entry : settings.entrySet()) {
      builder.setConfig(entry.getKey(), entry.getValue());
    }
    return this;
  }

  public Neo4jPersistModule config(Map<String, String> config) {
    builder.setConfig(config);
    return this;
  }

  @Override
  protected void configurePersistence() {
    bind(GraphDatabaseBuilder.class).annotatedWith(Neo4j.class).toInstance(builder);

    bind(PersistService.class).to(Neo4jPersistService.class);
    bind(UnitOfWork.class).to(Neo4jPersistService.class);
    bind(GraphDatabaseService.class).toProvider(Neo4jPersistService.class);
    
    transactionInterceptor = new Neo4jLocalTnxInterceptor();
    requestInjection(transactionInterceptor);
  }

  @Override
  protected MethodInterceptor getTransactionInterceptor() {
    return transactionInterceptor;
  }

  @Provides
  IndexManager provideIndexManager(GraphDatabaseService graph) {
    return graph.index();
  }
}
