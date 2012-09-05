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
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.RelationshipIndex;

import java.util.Map;

public class Neo4jIndexProviders {

  private Neo4jIndexProviders() {
    // non-instantiable
  }

  public static Provider<Index<Node>> indexForNodes(final String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    return new Provider<Index<Node>>() {
  
      @Inject IndexManager indexManager;
  
      @Override
      public Index<Node> get() {
        return indexManager.forNodes(name);
      }
    };
  }

  public static Provider<Index<Node>> indexForNodes(final String name, Map<String, String> config) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkNotNull(config);
  
    final Map<String, String> configToUse = ImmutableMap.copyOf(config);
  
    return new Provider<Index<Node>>() {
  
      @Inject IndexManager indexManager;
  
      @Override
      public Index<Node> get() {
        return indexManager.forNodes(name, configToUse);
      }
    };
  }

  public static Provider<RelationshipIndex> indexForRelationships(final String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    return new Provider<RelationshipIndex>() {
  
      @Inject IndexManager indexManager;
  
      @Override
      public RelationshipIndex get() {
        return indexManager.forRelationships(name);
      }
    };
  }

  public static Provider<RelationshipIndex> indexForRelationships(final String name, Map<String, String> config) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkNotNull(config);
  
    final Map<String, String> configToUse = ImmutableMap.copyOf(config);
  
    return new Provider<RelationshipIndex>() {
  
      @Inject IndexManager indexManager;
  
      @Override
      public RelationshipIndex get() {
        return indexManager.forRelationships(name, configToUse);
      }
    };
  }
}
