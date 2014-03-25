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

package net.wastl.rdfdot.base;

import com.google.common.base.Strings;
import net.wastl.rdfdot.GraphvizSerializer;
import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.config.Styles;
import net.wastl.rdfdot.exception.MaxSizeException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.*;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for graphviz serializers
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public abstract class GraphvizSerializerBase implements GraphvizSerializer {

    private GraphConfiguration configuration;

    protected Set<URI> uriNodes;
    protected Set<BNode> bnodeNodes;
    protected Set<Literal> literalNodes;
    protected Set<Statement> edges;

    protected String id = "G";

    protected boolean comments = true;

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

        if(id  != null) {
            this.id = id;
        }
    }

    /**
     * Serialize a node with the given value into this serializer. Should properly distinguish between
     * Literals, URIs and BNodes.
     *
     * @param v
     */
    @Override
    public void serializeNode(Value v) {
        if(v instanceof URI) {
            uriNodes.add((URI) v);
        } else if(v instanceof BNode) {
            bnodeNodes.add((BNode) v);
        } else if(v instanceof Literal) {
            literalNodes.add((Literal) v);
        }

        if(uriNodes.size() + bnodeNodes.size() + literalNodes.size() > configuration.getMaxNodes()) {
            throw new MaxSizeException("maximum number of nodes exceeded (max: "+configuration.getMaxNodes()+")");
        }
    }

    /**
     * Serialize an edge with the given value into this serializer. Implementations must take care that
     * the nodes used by the statement are also properly serialized.
     *
     * @param stmt
     */
    @Override
    public void serializeEdge(Statement stmt) {
        serializeNode(stmt.getSubject());
        serializeNode(stmt.getObject());

        edges.add(stmt);

        if(edges.size() > configuration.getMaxEdges()) {
            throw new MaxSizeException("maximum number of edges exceeded (max: "+configuration.getMaxEdges()+")");
        }
    }

    /**
     * Finish serializing the currently started graph, writing it out to any underlying writers.
     */
    @Override
    public void endGraph() {
        // write out the graph

        initSerialization();
        serializeGraphStart();
        serializeUris();
        serializeBNodes();
        serializeLiterals();
        serializeEdges();
        serializeGraphEnd();
    }


    protected void serializeGraphEnd() {
        addString("}\n");

        finishSerialization();
    }

    protected void serializeEdges() {
        // Edge configuration
        Options edgeOptions = new Options();
        edgeOptions.addOption("arrowhead", configuration.getArrowShape());
        edgeOptions.addOption("style", configuration.getArrowStyle());
        edgeOptions.addOption("color", graphvizColor(configuration.getArrowColor()));

        if(comments) {
            addString("\n  // edges\n");
        }
        addString("  edge[" + edgeOptions.toString() + "];\n");

        edges.forEach(edge -> addString("  " + getNodeID(edge.getSubject()) + " -> " + getNodeID(edge.getObject()) + "[label=\""+ StringEscapeUtils.escapeJava(getNodeLabel(edge.getPredicate())) +"\"];\n"));
    }

    protected void serializeLiterals() {
        if(literalNodes.size() > 0) {
            // Literal node configuration
            Options literalOptions = new Options();
            literalOptions.addOption("shape", configuration.getLiteralShape());
            literalOptions.addOption("style", configuration.getLiteralStyle());
            literalOptions.addOption("color", graphvizColor(configuration.getLiteralColor()));
            if (configuration.getLiteralFill() != null && configuration.getLiteralStyle() == Styles.FILLED) {
                literalOptions.addOption("fillcolor", graphvizColor(configuration.getLiteralFill()));
            }

            if(comments) {
                addString("\n  // literal nodes\n");
            }
            addString("  node[" + literalOptions.toString() + "];\n");

            // add all URI nodes
            literalNodes.forEach(node -> addString("  " + getNodeID(node) + " [label=\"" + StringEscapeUtils.escapeJava(getNodeLabel(node)) + "\"];\n"));
        }
    }

    protected void serializeBNodes() {
        if(bnodeNodes.size() > 0) {
            // BNode node configuration
            Options bnodeOptions = new Options();
            bnodeOptions.addOption("shape", configuration.getBnodeShape());
            bnodeOptions.addOption("style", configuration.getBnodeStyle());
            bnodeOptions.addOption("color", graphvizColor(configuration.getBnodeColor()));
            if (configuration.getBnodeFill() != null && configuration.getBnodeStyle() == Styles.FILLED) {
                bnodeOptions.addOption("fillcolor", graphvizColor(configuration.getBnodeFill()));
            }

            if(comments) {
                addString("\n  // blank nodes\n");
            }
            addString("  node[" + bnodeOptions.toString() + "];\n");

            // add all URI nodes
            bnodeNodes.forEach(node -> addString("  " + getNodeID(node) + " [label=\"" + StringEscapeUtils.escapeJava(getNodeLabel(node)) + "\"];\n"));
        }
    }

    protected void serializeUris() {
        if(uriNodes.size() > 0) {
            // URI node configuration
            Options uriOptions = new Options();
            uriOptions.addOption("shape", configuration.getUriShape());
            uriOptions.addOption("style", configuration.getUriStyle());
            uriOptions.addOption("color", graphvizColor(configuration.getUriColor()));
            if (configuration.getUriFill() != null && configuration.getUriStyle() == Styles.FILLED) {
                uriOptions.addOption("fillcolor", graphvizColor(configuration.getUriFill()));
            }

            if(comments) {
                addString("\n  // uri nodes\n");
            }
            addString("  node[" + uriOptions.toString() + "];\n");

            // add all URI nodes
            uriNodes.forEach(node -> addString("  " + getNodeID(node) + " [label=\"" + StringEscapeUtils.escapeJava(getNodeLabel(node)) + "\"];\n"));
        }
    }

    protected void serializeGraphStart() {
        // graph start
        addString("digraph ");
        addString(this.id);
        addString(" { \n");

        // graph settings
        addString("  rankdir=" + configuration.getLayout()+";\n");
    }

    protected String graphvizColor(Color c) {
        return String.format("\"#%02x%02x%02x\"",c.getRed(),c.getGreen(),c.getBlue());
    }


    protected String getNodeLabel(Value v) {
        if(v instanceof URI) {
            // resolve namespace if possible, otherwise return URI
            for(Map.Entry<String,String> ns : configuration.getNamespaces().entrySet()) {
                if(v.stringValue().startsWith(ns.getKey())) {
                    return ns.getValue() + ":" + v.stringValue().substring(ns.getKey().length());
                }
            }
        }
        return v.stringValue();
    }

    protected String getNodeID(Value v) {
        return "\"" + StringEscapeUtils.escapeJava(getNodeLabel(v)) + "\"";
    }

    protected abstract void initSerialization();

    protected abstract void addString(String data);

    protected abstract void finishSerialization();



    private static class Options {
        private Map<String,String> data = new HashMap<>();

        public void addOption(String key, Object value) {
            data.put(key,value.toString());
        }

        @Override
        public String toString() {
            return StringUtils.join(data.entrySet().stream().map(input -> input.getKey() + "=" + input.getValue()).collect(Collectors.toList()), ',');
        }
    }
}
