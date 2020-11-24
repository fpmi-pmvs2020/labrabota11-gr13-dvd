package drawer

import (
	"GraphDrawer/types"
	"fmt"
	"github.com/fogleman/gg"
	"github.com/golang/freetype/truetype"
	"golang.org/x/image/font/gofont/goregular"
	"image"
	"image/color"
	"math/rand"
	"time"
)

func GetHourRow (rowHeight, rowWidth int,
				 totalSpan types.TimeSpan) (img image.Image) {
	startTime := totalSpan.StartTime.Truncate(time.Hour)
	endTime := totalSpan.EndTime.Truncate(time.Hour).Add(time.Hour)

	hours := endTime.Sub(startTime).Truncate(time.Hour).Hours()
	tickWidth := float64(rowWidth) / hours

	context := gg.NewContext(rowWidth, rowHeight)
	context.SetRGB255(0xFF, 0xFF, 0xFF)
	context.Clear()

	font, _ := truetype.Parse(goregular.TTF)
	face := truetype.NewFace(font, &truetype.Options{Size: float64(rowHeight) / 2.0})
	context.SetFontFace(face)

	context.SetRGB255(0xB0, 0xB0, 0xB0)
	context.SetLineWidth(1.0)
	context.SetLineCapRound()
	context.SetDash(3.0, 3.0)
	currTime := startTime
	for i := 0; i < int(hours); i++ {
		context.SetRGB255(0xB0, 0xB0, 0xB0)
		context.DrawLine(float64(i) * tickWidth, 0,
						 float64(i) * tickWidth, float64(rowHeight))
		context.Stroke()

		context.SetRGB255(0, 0, 0)
		context.DrawStringAnchored(fmt.Sprintf("%02d:%02d", currTime.Hour(), currTime.Minute()),
								   float64(i) * tickWidth, float64(rowHeight) / 2.0, 0, 0.5)
	}

	return context.Image()
}

func GetGraphRow(rowHeight, rowWidth int,
				 personSpans []types.TimeSpan,
				 totalSpan types.TimeSpan) (c color.Color, img image.Image) {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))

	startTime := totalSpan.StartTime.Truncate(time.Hour)
	endTime := totalSpan.EndTime.Truncate(time.Hour).Add(time.Hour)

	hours := endTime.Sub(startTime).Truncate(time.Hour).Hours()
	tickWidth := float64(rowWidth) / 2.0 / hours
	c = color.RGBA{ R: uint8(random.Intn(0x100)), G: uint8(random.Intn(0x100)),
					B: uint8(random.Intn(0x100)), A: 0xFF }

	context := gg.NewContext(rowWidth, rowHeight)
	context.SetRGB255(0xFF, 0xFF, 0xFF)
	context.Clear()

	context.SetRGB255(0xB0, 0xB0, 0xB0)
	context.SetLineWidth(1.0)
	context.SetLineCapRound()
	context.SetDash(3.0, 3.0)
	for i := 0; i < 2 * int(hours); i++ {
		context.DrawLine(float64(i) * tickWidth, 0,
						 float64(i) * tickWidth, float64(rowHeight))
		context.Stroke()
	}

	context.SetColor(c)
	var spanStart, spanLength float64
	for _, span := range personSpans {
		spanStart = span.StartTime.Sub(startTime).Hours() * 2.0 * tickWidth
		spanLength = span.EndTime.Sub(span.StartTime).Hours() * 2.0 * tickWidth

		context.DrawRectangle(spanStart, float64(rowHeight) / 4.0, spanLength, float64(rowHeight) / 2.0)
		context.Fill()
	}

	return c, context.Image()
}