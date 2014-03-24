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

import net.wastl.rdfdot.base.GraphvizSerializerBase;
import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.string.GraphvizSerializerString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Add file description here!
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphvizSerializerCommand extends GraphvizSerializerString {

    private static Logger log = LoggerFactory.getLogger(GraphvizSerializerCommand.class);

    private String filename;

    public GraphvizSerializerCommand(GraphConfiguration configuration, String filename) {
        super(configuration);
        this.filename = filename;
    }


    @Override
    protected void finishSerialization() {
        render(getString(), filename);
    }

    private void render(String data, String filename) {
        log.info("rendering graph using external 'dot' command ...");

        long start = System.currentTimeMillis();

        Process p;
        try {
            p = Runtime.getRuntime().exec(String.format("dot -Tpng -o%s",filename));

            try(PrintWriter out = new PrintWriter(new OutputStreamWriter(p.getOutputStream()))) {
                out.print(data);
            }

            p.waitFor();

            log.info("finished ({}ms)!", System.currentTimeMillis()-start);
        } catch (IOException e) {
            log.error("could not execute dot command: {}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("command execution interrupted");
        }

    }
}
