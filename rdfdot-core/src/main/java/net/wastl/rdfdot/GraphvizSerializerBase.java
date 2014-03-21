/*
 * Copyright 2014 Sebastian Schaffert (wastl@wastl.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.wastl.rdfdot;

import net.wastl.rdfdot.config.GraphConfiguration;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for graphviz serializers
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public abstract class GraphvizSerializerBase implements GraphvizSerializer {

    private GraphConfiguration configuration;

    protected Set<Value> uriNodes, bnodeNodes, literalNodes;
    protected Set<Statement> edges;

    public GraphvizSerializerBase(GraphConfiguration configuration) {
        this.configuration = configuration;

    }


    /**
     * Return the graph configuration used by the serializer. Modifying this configuration will have
     * direct effect on the graph serializer.
     *
     * @return
     */
    @Override
    public GraphConfiguration getGraphConfiguration() {
        return configuration;
    }

    /**
     * Update the graph configuration used by the serializer.
     *
     * @param config
     */
    @Override
    public void setGraphConfiguration(GraphConfiguration config) {
        this.configuration = config;
    }

    /**
     * Start serializing a new graph. Subsequent calls of serializeNode and serializeEdge will build up the
     * graph. It should be written out latest when endGraph is called.
     *
     * @param id a graph ID (arbitrary alphanumeric identifier)
     */
    @Override
    public void startGraph(String id) {
        this.uriNodes = new HashSet<>();
        this.bnodeNodes = new HashSet<>();
        this.literalNodes = new HashSet<>();
        this.edges = new HashSet<>();

    }

    /**
     * Serialize a node with the given value into this serializer. Should properly distinguish between
     * Literals, URIs and BNodes.
     *
     * @param v
     */
    @Override
    public void serializeNode(Value v) {

    }

    /**
     * Serialize an edge with the given value into this serializer. Implementations must take care that
     * the nodes used by the statement are also properly serialized.
     *
     * @param stmt
     */
    @Override
    public void serializeEdge(Statement stmt) {

    }

    /**
     * Finish serializing the currently started graph, writing it out to any underlying writers.
     */
    @Override
    public void endGraph() {
        // write out the graph

        serializeGraphStart();
        serializeUris();
        serializeBNodes();
        serializeLiterals();
        serializeEdges();
        serializeGraphEnd();
    }

    protected abstract void serializeGraphEnd();

    protected abstract void serializeEdges();

    protected abstract void serializeLiterals();

    protected abstract void serializeBNodes();

    protected abstract void serializeUris();

    protected abstract void serializeGraphStart();
}
