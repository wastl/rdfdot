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

package net.wastl.rdfdot.render;

import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.string.GraphvizSerializerString;

/**
 * Add file description here!
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphvizSerializerNative extends GraphvizSerializerString {

    private String filename;

    public GraphvizSerializerNative(GraphConfiguration configuration, String filename) {
        super(configuration);
        this.filename = filename;
    }

    @Override
    protected void finishSerialization() {
        render(getString(), filename);
    }

    private native void render(String data, String filename);
}