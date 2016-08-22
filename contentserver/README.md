Readme
=======

[Goto wiki page for more information](/eh-dev/contentserver/wikis/home).


imagemagic:

Options used to compile and link:
  PREFIX          = /usr
  EXEC-PREFIX     = /usr
  VERSION         = 6.8.9
  CC              = gcc -std=gnu99 -std=gnu99
  CFLAGS          = -I/usr/include/OpenEXR -I/usr/include/freetype2 -fopenmp -g -O2 -Wall -march=core2 -fexceptions -pthread -DMAGICKCORE_HDRI_ENABLE=0 -DMAGICKCORE_QUANTUM_DEPTH=16
  CPPFLAGS        = -pthread -I/usr/include/pango-1.0 -I/usr/include/cairo -I/usr/include/glib-2.0 -I/usr/lib64/glib-2.0/include -I/usr/include/pixman-1 -I/usr/include/freetype2 -I/usr/include/libpng12 -DMAGICKCORE_HDRI_ENABLE=0 -DMAGICKCORE_QUANTUM_DEPTH=16 -I/usr/include/libxml2
  PCFLAGS         = 
  DEFS            = -DHAVE_CONFIG_H
  LDFLAGS         = 
  LIBS            = 
  CXX             = g++
  CXXFLAGS        = -g -O2 -pthread
  FEATURES        = DPC OpenMP
  DELEGATES       = bzlib djvu mpeg fftw fontconfig freetype jng jpeg lcms openexr pango png ps tiff webp x xml zlib
