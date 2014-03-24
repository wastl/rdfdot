#include <graphviz/gvc.h>
#include <stdio.h>
#include <jni.h>

extern gvplugin_library_t gvplugin_dot_layout_LTX_library;
extern gvplugin_library_t gvplugin_gd_LTX_library;
extern gvplugin_library_t gvplugin_core_LTX_library;

int main(char** argv) {
    const char* graph_data = "digraph g { a -> b; }";
    const char* filename   = "test.png";

    GVC_t* gvc;      // context
    Agraph_t* graph; // graph

    printf("GraphViz: building graph ...\n");

    gvc = gvContext();
    gvAddLibrary(gvc, &gvplugin_dot_layout_LTX_library);
    gvAddLibrary(gvc, &gvplugin_gd_LTX_library);
    gvAddLibrary(gvc, &gvplugin_core_LTX_library);

    graph = agmemread(graph_data);

    if(!graph) {
        printf("could not read graph\n");
        return;
    }

    printf("GraphViz: layouting graph ...\n");
    gvLayout(gvc,graph, "dot");

    printf("GraphViz: rendering graph to %s ...\n", filename);
    gvRenderFilename(gvc,graph,"png",filename);
    //gvRender(gvc, graph, "plain", stdout);

    printf("GraphViz: cleaning up ...\n");
    gvFreeLayout(gvc,graph);
    agclose(graph);
}