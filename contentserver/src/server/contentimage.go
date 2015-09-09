package server

import "github.com/gographics/imagick/imagick"

type ContentImage struct {
	MW *imagick.MagickWand
}

func NewImage() *ContentImage {
	return &ContentImage{MW: imagick.NewMagickWand()}
}

func (img *ContentImage) Destroy() {
	img.MW.Destroy()
}
