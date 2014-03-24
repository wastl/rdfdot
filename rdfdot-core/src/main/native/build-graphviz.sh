#!/bin/bash
SCRIPT=`readlink -f $0`
SCRDIR=`dirname $SCRIPT`
CURDIR=`pwd`
WORKDIR=`readlink -f $SCRDIR/../../..`/target/native/build
INSTDIR=`readlink -f $SCRDIR/../../..`/target/native/graphviz

echo "======================================================================="
echo "= GraphViz: building static library for use in JNI ..."
echo "======================================================================="
echo "Configuration:"
echo "- script directory: $CURDIR"
echo "- work directory: $WORKDIR"

mkdir -p $WORKDIR

GRAPHVIZ_HREF="http://www.graphviz.org/pub/graphviz/stable/SOURCES/graphviz-2.36.0.tar.gz"
LIBGD_HREF="https://bitbucket.org/libgd/gd-libgd/downloads/libgd-2.1.0.tar.gz"
LIBPNG_HREF="http://freefr.dl.sourceforge.net/project/libpng/libpng12/1.2.51/libpng-1.2.51.tar.gz"
LIBJPEG_HREF="http://www.ijg.org/files/jpegsrc.v9a.tar.gz"
FREETYPE_HREF="http://download.savannah.gnu.org/releases/freetype/freetype-2.5.3.tar.gz"
FONTCONFIG_HREF="http://www.freedesktop.org/software/fontconfig/release/fontconfig-2.11.1.tar.gz"

cd $WORKDIR

export CFLAGS="-O2 -fPIC -fno-omit-frame-pointer -I$INSTDIR/include"
export LDFLAGS="-L$INSTDIR/lib"

function download {
    echo "downloading $1"
    curl -L -C - -o $1.tar.gz "$2"
}

function build {
    echo "building $1 ..."
    mkdir $1
    cd $1
    
    echo " - unpacking ..."
    tar xz --strip-components=1 -f ../$1.tar.gz 

    echo " - configuring ..."
    CFLAGS="$CFLAGS" ./configure --prefix=$INSTDIR --enable-static $2 > ../$1.log 2>&1

    echo " - building ..."
    make -j 4 > ../$1-build.log 2>&1 || true

    echo " - installing ..."
    make install > ../$1-install.log 2>&1 || true

    cd ..
}

echo "======================================================================="
echo "= downloading libraries ..."
echo "======================================================================="

download graphviz   "$GRAPHVIZ_HREF"
download libgd      "$LIBGD_HREF"
download libpng     "$LIBPNG_HREF"
download libjpeg    "$LIBJPEG_HREF"
download fontconfig "$FONTCONFIG_HREF"
download freetype   "$FREETYPE_HREF"

echo "======================================================================="
echo "= building libraries ..."
echo "======================================================================="

build libpng
build libjpeg
build freetype 
build fontconfig "--sysconfdir=/etc"
build libgd      "--without-tiff --without-xpm --without-vpx"
build graphviz   "--with-pango=no --with-qt=no --with-pangocairo=no --with-gtk=no --without-x --with-gdk=no --with-expat=no --with-gdk-pixbuf=no --with-rsvg=no --with-gd=/tmp/graphviz --with-included-ltdl"

cd $CURDIR
