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

#include <graphviz/gvc.h>
#include <stdio.h>
#include <jni.h>
#include "GraphvizSerializerNative.h"

extern gvplugin_library_t gvplugin_dot_layout_LTX_library;
extern gvplugin_library_t gvplugin_gd_LTX_library;
extern gvplugin_library_t gvplugin_core_LTX_library;

JNIEXPORT jbyteArray JNICALL Java_net_wastl_rdfdot_render_GraphvizSerializerNative_render(JNIEnv *env, jobject serializer, jstring jgraph) {
    // copy over the graph specification from the Java String to a C char array
    const char* graph_data = (*env)->GetStringUTFChars(env,jgraph,NULL);

    GVC_t* gvc;      // context
    Agraph_t* graph; // graph

    char* result_data = NULL;   // resulting byte array, allocated by gvRenderData
    int   result_length = 0;    // length of resulting byte array, set by gvRenderData

    jbyteArray result;

    printf("GraphViz: building graph ...\n");

    // setup graphviz context
    gvc = gvContext();

    // add the plugin libraries explicitly, since we have no dynamic loading
    gvAddLibrary(gvc, &gvplugin_dot_layout_LTX_library);
    gvAddLibrary(gvc, &gvplugin_gd_LTX_library);
    gvAddLibrary(gvc, &gvplugin_core_LTX_library);

    // read graph from in-memory string representation
    graph = agmemread(graph_data);

    if(!graph) {
        printf("could not read graph\n");
        return;
    }

    // call graphviz dot layouter
    printf("GraphViz: layouting graph ...\n");
    gvLayout(gvc,graph, "dot");

    // render graph to in-memory byte array
    printf("GraphViz: rendering graph ...\n");
    gvRenderData(gvc, graph, "png", &result_data, &result_length);

    // copy data over to java byte array
    printf("GraphViz: copying data (%d bytes)\n", result_length);
    result = (*env)->NewByteArray(env,result_length);
    (*env)->SetByteArrayRegion(env,result,0,result_length,(jbyte*)result_data);
    gvFreeRenderData(result_data);

    // free resources
    printf("GraphViz: cleaning up ...\n");
    gvFreeLayout(gvc,graph);
    agclose(graph);

    (*env)->ReleaseStringUTFChars(env,jgraph,graph_data);

    return result;
}