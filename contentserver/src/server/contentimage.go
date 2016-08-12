package server

import "gopkg.in/gographics/imagick.v2/imagick"

type ContentImage struct {
	MW *imagick.MagickWand
}

func NewImage() *ContentImage {
	return &ContentImage{MW: imagick.NewMagickWand()}
}

func (img *ContentImage) Destroy() {
	img.MW.Destroy()
}
