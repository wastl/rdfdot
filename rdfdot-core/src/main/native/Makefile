GRAPHVIZ_BUILD=`readlink -f ../../..`/target/native/build
GRAPHVIZ_HOME=`readlink -f ../../..`/target/native/graphviz
JAVA_HOME=/usr/lib/jvm/java-8-oracle
LDFLAGS=-L$(GRAPHVIZ_HOME)/lib -L$(GRAPHVIZ_HOME)/lib/graphviz -Wl,-Bstatic -Wl,--whole-archive -lcgraph -lgvc -lcdt -lpathplan -lgvplugin_core -lgvplugin_dot_layout -lgvplugin_gd -lgd -lpng12 -ljpeg -lfreetype -lfontconfig -lexpat -lz -Wl,--no-whole-archive -Wl,-Bdynamic -static-libgcc -lm -ldl
CFLAGS=-O2 -fPIC -pthread -D_REENTRANT -fno-omit-frame-pointer -I../native -I$(GRAPHVIZ_HOME)/include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux 

all: libgraphviz

libgraphviz: graphviz GraphvizSerializerNative.o
	gcc GraphvizSerializerNative.o -dynamic -shared -o libgraphviz.so $(LDFLAGS)

.o: 
	gcc -c $(CFLAGS) $< -o $@

graphviz:
	./build-graphviz.sh

clean:
	rm -f *.o
	rm -f *.so
#	rm -fr $GRAPHVIZ_BUILD

