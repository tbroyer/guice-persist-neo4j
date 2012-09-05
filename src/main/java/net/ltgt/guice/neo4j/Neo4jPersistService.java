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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;

@Singleton
class Neo4jPersistService implements Provider<GraphDatabaseService>, PersistService, UnitOfWork {

  private final GraphDatabaseBuilder builder;

  private GraphDatabaseService graph;

  @Inject
  public Neo4jPersistService(@Neo4j GraphDatabaseBuilder builder) {
    this.builder = builder;
  }

  public GraphDatabaseService get() {
    return graph;
  }

  public void begin() {
    // Neo4j has no notion of unit-of-work
  }

  public void end() {
    // Neo4j has no notion of unit-of-work
  }

  public void start() {
    Preconditions.checkState(graph == null, "Persistence service was already initialized.");
    graph = builder.newGraphDatabase();
  }

  public void stop() {
    if (graph != null) {
      graph.shutdown();
    }
  }
}
