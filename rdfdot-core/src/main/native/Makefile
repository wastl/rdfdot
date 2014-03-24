GRAPHVIZ_HOME=/tmp/graphviz
JAVA_HOME=/usr/lib/jvm/java-8-oracle
LDFLAGS=-L$(GRAPHVIZ_HOME)/lib -L$(GRAPHVIZ_HOME)/lib/graphviz -Wl,-Bstatic -Wl,--whole-archive -lcgraph -lgvc -lcdt -lpathplan -lgvplugin_core -lgvplugin_dot_layout -lgvplugin_gd -lgd -lpng12 -ljpeg -lfreetype -lfontconfig -Wl,--no-whole-archive -Wl,-Bdynamic -lm -lltdl -ldl
CFLAGS=-O2 -fPIC -pthread -D_REENTRANT -fno-omit-frame-pointer -I../native -I$(GRAPHVIZ_HOME)/include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux 

all: libgraphviz

libgraphviz: GraphvizSerializerNative.o
	gcc GraphvizSerializerNative.o -dynamic -shared -o libgraphviz.so $(LDFLAGS)

.o: 
	gcc -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o
	rm -f *.so
