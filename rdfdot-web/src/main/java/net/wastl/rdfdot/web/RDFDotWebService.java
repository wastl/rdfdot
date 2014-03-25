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

package net.wastl.rdfdot.web;

import net.wastl.rdfdot.GraphvizHandler;
import net.wastl.rdfdot.GraphvizSerializer;
import net.wastl.rdfdot.config.Arrows;
import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.config.Shapes;
import net.wastl.rdfdot.config.Styles;
import net.wastl.rdfdot.render.GraphvizSerializerCommand;
import net.wastl.rdfdot.render.GraphvizSerializerNative;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.rio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Web Service for rendering RDF graphs
 *
 * @author Sebastian Schaffert (sschaffert@apache.org)
 */
@Path("/")
public class RDFDotWebService {

    private static Logger log = LoggerFactory.getLogger(RDFDotWebService.class);

    @POST
    @Path("/render")
    public Response renderRDF(@QueryParam("input") String input,
                              @QueryParam("output") String output,
                              @QueryParam("backend") String backend,
                              @QueryParam("base64") Boolean base64,
                              @QueryParam("uri_shape") Shapes uri_shape,
                              @QueryParam("uri_fill")  String uri_fill,
                              @QueryParam("uri_style") Styles uri_style,
                              @QueryParam("bnode_shape") Shapes bnode_shape,
                              @QueryParam("bnode_fill")  String bnode_fill,
                              @QueryParam("bnode_style") Styles bnode_style,
                              @QueryParam("literal_shape") Shapes literal_shape,
                              @QueryParam("literal_fill")  String literal_fill,
                              @QueryParam("literal_style") Styles literal_style,
                              @QueryParam("arrow_shape") Arrows arrow_shape,
                              @QueryParam("arrow_color")  String arrow_color,
                              @QueryParam("arrow_style") Styles arrow_style,
                              @Context HttpServletRequest request) {
        if(backend == null) {
            backend = "native";
        }
        if(output == null) {
            output = "png";
        }
        if(input == null) {
            input = "turtle";
        }

        GraphConfiguration configuration = new GraphConfiguration();

        if(uri_shape != null) configuration.setUriShape(uri_shape);
        if(uri_fill  != null) configuration.setUriFill(parseColor(uri_fill));
        if(uri_style != null) configuration.setUriStyle(uri_style);
        if(bnode_shape != null) configuration.setBnodeShape(bnode_shape);
        if(bnode_fill  != null) configuration.setBnodeFill(parseColor(bnode_fill));
        if(bnode_style != null) configuration.setBnodeStyle(bnode_style);
        if(literal_shape != null) configuration.setLiteralShape(literal_shape);
        if(literal_fill  != null) configuration.setLiteralFill(parseColor(literal_fill));
        if(literal_style != null) configuration.setLiteralStyle(literal_style);
        if(arrow_shape != null) configuration.setArrowShape(arrow_shape);
        if(arrow_color  != null) configuration.setArrowColor(parseColor(arrow_color));
        if(arrow_style != null) configuration.setArrowStyle(arrow_style);

        try {
                GraphvizSerializer serializer;
                if (StringUtils.equalsIgnoreCase(backend, "native")) {
                    serializer = new GraphvizSerializerNative(configuration);
                } else {
                    File tmpFile = File.createTempFile("rdfdot", ".png");
                    serializer = new GraphvizSerializerCommand(configuration, tmpFile.getAbsolutePath());
                    tmpFile.deleteOnExit();
                }

                RDFParser parser = Rio.createParser(RDFFormat.forMIMEType(getRDFMimeType(input)));
                parser.setRDFHandler(new GraphvizHandler(serializer));
                parser.parse(request.getInputStream(), "http://localhost/");

                if(base64 != null && base64) {
                    return Response.ok((StreamingOutput) stream -> IOUtils.write(serializer.getResult(), new Base64OutputStream(stream)))
                            .header("Content-Type", "application/base64").build();
                } else {
                    return Response.ok((StreamingOutput) stream -> IOUtils.write(serializer.getResult(), stream))
                            .header("Content-Type", getImageMimeType(output)).build();
                }
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        } catch (RDFHandlerException e) {
            return Response.serverError().entity(e.getMessage()).build();
        } catch (RDFParseException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    private String getRDFMimeType(String input) {
        switch (input) {
            case "turtle":
                return "text/turtle";
            case "xml":
                return "application/rdf+xml";
            default:
                return "application/rdf+xml";
        }
    }

    private String getImageMimeType(String output) {
        switch (output) {
            case "png":
                return "image/png";
            case "jpeg":
                return "image/jpeg";
            default:
                return "image/png";
        }
    }

    private Color parseColor(String hex) {
        return new Color(Integer.parseInt(hex.substring(1), 16));
    }
}
