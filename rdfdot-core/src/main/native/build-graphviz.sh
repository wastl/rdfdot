#!/bin/bash
SCRIPT=`readlink -f $0`
SCRDIR=`dirname $SCRIPT`
CURDIR=`pwd`
DLDIR=`readlink -f $SCRDIR/../../..`/target/native/download
WORKDIR=`readlink -f $SCRDIR/../../..`/target/native/build
INSTDIR=`readlink -f $SCRDIR/../../..`/target/native/graphviz

if [ -f "$INSTDIR/lib/libgvc.a" ]; then
    echo "GraphViz library already build ..."
    exit 0
fi

echo "======================================================================="
echo "= GraphViz: building static library for use in JNI ..."
echo "======================================================================="
echo "Configuration:"
echo "- script directory: $CURDIR"
echo "- work directory: $WORKDIR"

mkdir -p $WORKDIR
mkdir -p $DLDIR

GRAPHVIZ_HREF="http://www.graphviz.org/pub/graphviz/stable/SOURCES/graphviz-2.36.0.tar.gz"
GRAPHVIZ_MD5=1f41664dba0c93109ac8b71216bf2b57

LIBGD_HREF="https://bitbucket.org/libgd/gd-libgd/downloads/libgd-2.1.0.tar.gz"
LIBGD_MD5=20a8eeae51cef7d7850679b1e53ce2b5

LIBPNG_HREF="http://freefr.dl.sourceforge.net/project/libpng/libpng12/1.2.51/libpng-1.2.51.tar.gz"
LIBPNG_MD5=e358f9a265f2063b36f10dc454ee0e17

LIBJPEG_HREF="http://www.ijg.org/files/jpegsrc.v9a.tar.gz"
LIBJPEG_MD5=3353992aecaee1805ef4109aadd433e7

FREETYPE_HREF="http://download.savannah.gnu.org/releases/freetype/freetype-2.5.3.tar.gz"
FREETYPE_MD5=cafe9f210e45360279c730d27bf071e9

FONTCONFIG_HREF="http://www.freedesktop.org/software/fontconfig/release/fontconfig-2.11.1.tar.gz"
FONTCONFIG_MD5=e75e303b4f7756c2b16203a57ac87eba

EXPAT_HREF="http://heanet.dl.sourceforge.net/project/expat/expat/2.1.0/expat-2.1.0.tar.gz"
EXPAT_MD5=dd7dab7a5fea97d2a6a43f511449b7cd

ZLIB_HREF="http://zlib.net/zlib-1.2.8.tar.gz"
ZLIB_MD5=44d667c142d7cda120332623eab69f40

cd $WORKDIR

export CFLAGS="-O2 -fPIC -fno-omit-frame-pointer -I$INSTDIR/include"
export LDFLAGS="-L$INSTDIR/lib"
export PKG_CONFIG_PATH=$INSTDIR/lib/pkgconfig:/usr/lib/pkgconfig
export PATH=$INSTDIR/bin:$PATH

function download {
    cd $DLDIR

    if [ ! -f "$1.tar.gz" ] || [ "`md5sum $1.tar.gz | cut -f 1 -d ' '`" != "$3" ]; then
	rm -f $1.tar.gz
    	echo "downloading $1"
	curl -L -C - -o $1.tar.gz "$2"

	if [ "`md5sum $1.tar.gz | cut -f 1 -d ' '`" != "$3" ]; then
		echo "MD5 mismatch for file $1.tar.gz"
		exit 1
	fi
    fi
}

function build {
    cd $WORKDIR
    echo "building $1 ..."
    rm -fR $1
    mkdir -p $1
    cd $1
    
    echo " - unpacking ..."
    tar xz --strip-components=1 -f $DLDIR/$1.tar.gz 

    echo " - configuring ..."
    CFLAGS="$CFLAGS" ./configure --prefix=$INSTDIR $2 > ../$1-configure.log 2>&1

    echo " - building ..."
    make -j 4 > ../$1-build.log 2>&1 || true

    echo " - installing ..."
    make install > ../$1-install.log 2>&1 || true

    cd ..
}

echo "======================================================================="
echo "= downloading libraries ..."
echo "======================================================================="

download graphviz   "$GRAPHVIZ_HREF"   "$GRAPHVIZ_MD5"
download libgd      "$LIBGD_HREF"      "$LIBGD_MD5"
download libpng     "$LIBPNG_HREF"     "$LIBPNG_MD5"
download libjpeg    "$LIBJPEG_HREF"    "$LIBJPEG_MD5"
download fontconfig "$FONTCONFIG_HREF" "$FONTCONFIG_MD5"
download freetype   "$FREETYPE_HREF"   "$FREETYPE_MD5"
download expat      "$EXPAT_HREF"      "$EXPAT_MD5"
download zlib       "$ZLIB_HREF"       "$ZLIB_MD5"

echo "======================================================================="
echo "= building libraries ..."
echo "======================================================================="

build zlib       "--static"
build expat      "--enable-static"
build libpng     "--enable-static"
build libjpeg    "--enable-static"
build freetype   "--enable-static --with-harfbuzz=no"
build fontconfig "--enable-static --sysconfdir=/etc"
build libgd      "--enable-static --without-tiff --without-xpm --without-vpx --with-png=$INSTDIR --with-freetype=$INSTDIR --with-fontconfig=$INSTDIR"
build graphviz   "--enable-static --with-pango=no --with-qt=no --with-pangocairo=no --with-gtk=no --without-x --with-gdk=no --with-expat=$INSTDIR --with-gdk-pixbuf=no --with-rsvg=no --with-gd=$INSTDIR --with-included-ltdl"

cd $CURDIR
