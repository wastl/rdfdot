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
 * A reasonable selection of GraphViz node and edge styles for use in RDF graphs
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public enum Styles {
    SOLID("solid"), DASHED("dashed"), DOTTED("dotted"), BOLD("bold"), FILLED("filled");

    private String cfg;

    Styles(String cfg) {
        this.cfg = cfg;
    }

    @Override
    public String toString() {
        return cfg;
    }
}
