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

/**
 * A reasonable selection of possible GraphViz node shapes for use in RDF graphs.
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public enum Shapes {

    BOX("box"), SQUARE("square"), ELLIPSE("ellipse"), OVAL("oval"), CIRCLE("circle"), EGG("egg"), POINT("point"), OCTAGON("octagon"), PLAINTEXT("plaintext");


    private String cfg;

    Shapes(String cfg) {
        this.cfg = cfg;
    }

    @Override
    public String toString() {
        return cfg;
    }
}
