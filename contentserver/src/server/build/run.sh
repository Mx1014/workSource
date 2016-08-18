#!/bin/sh
#export PKG_CONFIG_PATH=/usr/lib/pkgconfig
#golang 1.7
export PATH=$PATH:/usr/local/ffmpeg/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/ffmpeg/lib
./main $*
