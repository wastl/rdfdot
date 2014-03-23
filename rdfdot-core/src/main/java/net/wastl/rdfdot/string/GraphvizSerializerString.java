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

package net.wastl.rdfdot.string;

import net.wastl.rdfdot.base.GraphvizSerializerBase;
import net.wastl.rdfdot.config.GraphConfiguration;

/**
 * Add file description here!
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphvizSerializerString extends GraphvizSerializerBase {

    StringBuilder builder;

    public GraphvizSerializerString(GraphConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void initSerialization() {
        builder = new StringBuilder();
    }

    @Override
    protected void addString(String data) {
        builder.append(data);
    }

    @Override
    protected void finishSerialization() {
        // nothing to do
    }

    public String getString() {
        if(builder != null) {
            return builder.toString();
        } else {
            return null;
        }
    }
}
