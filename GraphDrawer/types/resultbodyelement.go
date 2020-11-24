package types

import (
	"encoding/base64"
	"image"
	"image/png"
	"strings"
)

func (rbe *ResultBodyElement) SavePNGImage(img image.Image) (err error) {
	var buffer strings.Builder
	writer := base64.NewEncoder(base64.StdEncoding, &buffer)
	if err = png.Encode(writer, img); err != nil {
		return err
	}
	if err = writer.Close(); err != nil {
		return err
	}

	rbe.FileString = buffer.String()
	return nil
}
