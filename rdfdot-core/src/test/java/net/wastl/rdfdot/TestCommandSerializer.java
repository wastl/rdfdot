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
import net.wastl.rdfdot.render.GraphvizSerializerCommand;
import net.wastl.rdfdot.render.GraphvizSerializerNative;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.rio.*;

import java.io.File;
import java.io.IOException;

/**
 * Add file description here!
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
public class TestCommandSerializer {

    public static final String FILENAME = "/tmp/test-command.png";

    private GraphvizSerializerCommand serializer;
    private RDFParser                 parser;


    @Before
    public void setup() {
        (new File(FILENAME)).delete();

        serializer = new GraphvizSerializerCommand(new GraphConfiguration(), FILENAME);
        parser     = Rio.createParser(RDFFormat.TURTLE);
        parser.setRDFHandler(new GraphvizHandler(serializer));
    }


    @Test
    public void testDefault() throws RDFParseException, IOException, RDFHandlerException {
        parser.parse(this.getClass().getResourceAsStream("/example1.ttl"),"");

        byte[] image = serializer.getResult();
        Assert.assertTrue(image.length > 0);
    }
}
