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

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder.DatabaseCreator;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;

public class RestNeo4jPersistModule extends Neo4jPersistModule {

  public RestNeo4jPersistModule(DatabaseCreator creator) {
    super(creator);
  }

  @Provides
  RestGraphDatabase provideRestGraphDatabase(GraphDatabaseService graph) {
    return (RestGraphDatabase) graph;
  }

  @Provides
  RestAPI provideRestAPI(RestGraphDatabase graph) {
    return graph.getRestAPI();
  }
}
