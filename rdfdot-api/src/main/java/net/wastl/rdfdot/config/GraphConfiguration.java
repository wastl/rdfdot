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

package net.wastl.rdfdot.config;

import org.openrdf.model.vocabulary.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration object for representing graph formatting configuration.
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphConfiguration {

    /**
     * Shape used for arrow heads of triples
     */
    private Arrows arrowShape = Arrows.VEE;

    /**
     * Shape used for RDF URI nodes
     */
    private Shapes uriShape   = Shapes.OVAL;

    /**
     * Shape used for RDF BNode nodes
     */
    private Shapes bnodeShape = Shapes.POINT;


    /**
     * Shape used for RDF literal nodes
     */
    private Shapes literalShape = Shapes.BOX;


    /**
     * Line style for arrows
     */
    private Styles arrowStyle   = Styles.SOLID;

    /**
     * Style for RDF URI nodes
     */
    private Styles uriStyle     = Styles.SOLID;

    /**
     * Style for RDF BNode nodes
     */
    private Styles bnodeStyle   = Styles.SOLID;


    /**
     * Style for RDF literal nodes
     */
    private Styles literalStyle = Styles.FILLED;


    /**
     * Line color used for arrows
     */
    private Color arrowColor    = Color.BLACK;

    /**
     * Line color used for RDF URI nodes
     */
    private Color uriColor      = Color.BLACK;

    /**
     * Line color used for RDF BNode nodes
     */
    private Color bnodeColor    = Color.BLACK;

    /**
     * Line color used for RDF BNode nodes
     */
    private Color literalColor  = Color.BLACK;

    /**
     * Fill color for RDF URI nodes
     */
    private Color uriFill       = null;

    /**
     * Fill color for RDF BNode nodes
     */
    private Color bnodeFill     = null;

    /**
     * Fill color for RDF literal nodes
     */
    private Color literalFill   = new Color(178, 76, 255);


    private Layouts layout      = Layouts.LEFT_RIGHT;


    private Map<String,String> namespaces;


    private int maxNodes = 50;
    private int maxEdges = 100;

    public GraphConfiguration() {
        // initialise default namespaces
        this.namespaces = new HashMap<>();

        namespaces.put(DCTERMS.NAMESPACE, DCTERMS.PREFIX);
        namespaces.put(RDF.NAMESPACE, RDF.PREFIX);
        namespaces.put(RDFS.NAMESPACE, RDFS.PREFIX);
        namespaces.put(OWL.NAMESPACE, OWL.PREFIX);
        namespaces.put(FOAF.NAMESPACE, "foaf");
        namespaces.put(SKOS.NAMESPACE, SKOS.PREFIX);
    }


    public void addNamespace(String uri, String prefix) {
        namespaces.put(uri,prefix);
    }


    public String getNamespace(String uri) {
        return namespaces.get(uri);
    }


    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    /**
     * Shape used for arrow heads of triples
     */
    public Arrows getArrowShape() {
        return arrowShape;
    }

    /**
     * Shape used for arrow heads of triples
     */
    public void setArrowShape(Arrows arrowShape) {
        this.arrowShape = arrowShape;
    }

    /**
     * Shape used for RDF URI nodes
     */
    public Shapes getUriShape() {
        return uriShape;
    }

    /**
     * Shape used for RDF URI nodes
     */
    public void setUriShape(Shapes uriShape) {
        this.uriShape = uriShape;
    }

    /**
     * Shape used for RDF BNode nodes
     */
    public Shapes getBnodeShape() {
        return bnodeShape;
    }

    /**
     * Shape used for RDF BNode nodes
     */
    public void setBnodeShape(Shapes bnodeShape) {
        this.bnodeShape = bnodeShape;
    }

    /**
     * Shape used for RDF literal nodes
     */
    public Shapes getLiteralShape() {
        return literalShape;
    }

    /**
     * Shape used for RDF literal nodes
     */
    public void setLiteralShape(Shapes literalShape) {
        this.literalShape = literalShape;
    }

    /**
     * Line style for arrows
     */
    public Styles getArrowStyle() {
        return arrowStyle;
    }

    /**
     * Line style for arrows
     */
    public void setArrowStyle(Styles arrowStyle) {
        this.arrowStyle = arrowStyle;
    }

    /**
     * Style for RDF URI nodes
     */
    public Styles getUriStyle() {
        return uriStyle;
    }

    /**
     * Style for RDF URI nodes
     */
    public void setUriStyle(Styles uriStyle) {
        this.uriStyle = uriStyle;
    }

    /**
     * Style for RDF BNode nodes
     */
    public Styles getBnodeStyle() {
        return bnodeStyle;
    }

    /**
     * Style for RDF BNode nodes
     */
    public void setBnodeStyle(Styles bnodeStyle) {
        this.bnodeStyle = bnodeStyle;
    }

    /**
     * Style for RDF literal nodes
     */
    public Styles getLiteralStyle() {
        return literalStyle;
    }

    /**
     * Style for RDF literal nodes
     */
    public void setLiteralStyle(Styles literalStyle) {
        this.literalStyle = literalStyle;
    }

    /**
     * Line color used for arrows
     */
    public Color getArrowColor() {
        return arrowColor;
    }

    /**
     * Line color used for arrows
     */
    public void setArrowColor(Color arrowColor) {
        this.arrowColor = arrowColor;
    }

    /**
     * Line color used for RDF URI nodes
     */
    public Color getUriColor() {
        return uriColor;
    }

    /**
     * Line color used for RDF URI nodes
     */
    public void setUriColor(Color uriColor) {
        this.uriColor = uriColor;
    }

    /**
     * Line color used for RDF BNode nodes
     */
    public Color getBnodeColor() {
        return bnodeColor;
    }

    /**
     * Line color used for RDF BNode nodes
     */
    public void setBnodeColor(Color bnodeColor) {
        this.bnodeColor = bnodeColor;
    }

    /**
     * Line color used for RDF BNode nodes
     */
    public Color getLiteralColor() {
        return literalColor;
    }

    /**
     * Line color used for RDF BNode nodes
     */
    public void setLiteralColor(Color literalColor) {
        this.literalColor = literalColor;
    }

    /**
     * Fill color for RDF URI nodes
     */
    public Color getUriFill() {
        return uriFill;
    }

    /**
     * Fill color for RDF URI nodes
     */
    public void setUriFill(Color uriFill) {
        this.uriFill = uriFill;
    }

    /**
     * Fill color for RDF BNode nodes
     */
    public Color getBnodeFill() {
        return bnodeFill;
    }

    /**
     * Fill color for RDF BNode nodes
     */
    public void setBnodeFill(Color bnodeFill) {
        this.bnodeFill = bnodeFill;
    }

    /**
     * Fill color for RDF literal nodes
     */
    public Color getLiteralFill() {
        return literalFill;
    }

    /**
     * Fill color for RDF literal nodes
     */
    public void setLiteralFill(Color literalFill) {
        this.literalFill = literalFill;
    }

    /**
     * Layout for graph drawing (currently specified the direction)
     * @return
     */
    public Layouts getLayout() {
        return layout;
    }

    /**
     * Layout for graph drawing (currently specified the direction)
     */
    public void setLayout(Layouts layout) {
        this.layout = layout;
    }


    public int getMaxNodes() {
        return maxNodes;
    }

    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }

    public int getMaxEdges() {
        return maxEdges;
    }

    public void setMaxEdges(int maxEdges) {
        this.maxEdges = maxEdges;
    }
}
