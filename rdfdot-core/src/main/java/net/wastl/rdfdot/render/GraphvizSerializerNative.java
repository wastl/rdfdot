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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of Graphviz Serializer using the Java JNI native library included in the source code (currently
 * works under Linux x64 only).
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class GraphvizSerializerNative extends GraphvizSerializerString {
    private static Logger log = LoggerFactory.getLogger(GraphvizSerializerNative.class);

    public static boolean available = false;

    static {
        try {
            System.loadLibrary( "graphviz" );
            available = true;
        }
        catch( UnsatisfiedLinkError e ) {
            System.out.println( "Could not load native code for rendering." );
        }
    }

    private static ReentrantLock renderLock = new ReentrantLock();


    private String filename;

    public GraphvizSerializerNative(GraphConfiguration configuration, String filename) {
        super(configuration);
        this.filename = filename;
    }

    @Override
    protected void finishSerialization() {
        renderLock.lock();
        try {
            log.info("rendering graph using native library call ...");
            //log.debug("Graph: {}", getString());
            long start = System.currentTimeMillis();
            render(getString(), filename);
            log.info("finished ({}ms)!", System.currentTimeMillis()-start);
        } finally {
            renderLock.unlock();
        }
    }

    private native void render(String data, String filename);
}
