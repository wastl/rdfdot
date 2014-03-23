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

package net.wastl.rdfdot.stream;

import net.wastl.rdfdot.base.GraphvizSerializerBase;
import net.wastl.rdfdot.config.GraphConfiguration;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Add file description here!
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphvizSerializerStream extends GraphvizSerializerBase {

    private PrintWriter out;


    public GraphvizSerializerStream(GraphConfiguration configuration, OutputStream outputStream) {
        super(configuration);

        this.out = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    public GraphvizSerializerStream(GraphConfiguration configuration, Writer out) {
        super(configuration);
        this.out = new PrintWriter(out);
    }


    @Override
    protected void initSerialization() {
        // nothing to do
    }

    @Override
    protected void addString(String data) {
        out.print(data);
    }

    @Override
    protected void finishSerialization() {
        out.close();
    }
}
