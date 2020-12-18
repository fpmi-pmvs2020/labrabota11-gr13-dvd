package drawer

import (
	"GraphDrawer/types"
	"fmt"
	"github.com/fogleman/gg"
	"github.com/golang/freetype/truetype"
	"golang.org/x/image/font"
	"golang.org/x/image/font/gofont/goregular"
	"image"
	"image/color"
	"image/draw"
	"math/rand"
	"time"
)

func GetHourRow (rowHeight, rowWidth int,
				 totalSpan types.TimeSpan) (img image.Image) {
	startTime := totalSpan.StartTime.Truncate(time.Hour)
	endTime := totalSpan.EndTime.Truncate(time.Hour)
	if !endTime.Equal(totalSpan.EndTime) {
		endTime = endTime.Add(time.Hour)
	}

	gofont, _ := truetype.Parse(goregular.TTF)
	face := truetype.NewFace(gofont, &truetype.Options{Size: 2.0 * float64(rowHeight) / 5.0})

	hours := endTime.Sub(startTime).Truncate(time.Hour).Hours()
	tickWidth := float64(rowWidth) / 2.0 / hours
	wordWidth := float64(font.MeasureString(face, "00").Ceil())

	context := gg.NewContext(rowWidth, rowHeight)
	context.SetRGBA255(0xFF, 0xFF, 0xFF, 0)
	context.Clear()

	context.SetFontFace(face)

	context.SetLineWidth(2.0)
	context.SetLineCapRound()
	currTime := startTime
	currWordWidth := 0.0

	for i := 0; i <= int(hours); i++ {
		context.SetRGB255(0xB0, 0xB0, 0xB0)
		context.DrawLine(float64(2 * i) * tickWidth + 1, 4.0 * float64(rowHeight) / 5.0,
						 float64(2 * i) * tickWidth + 1, float64(rowHeight))
		context.Stroke()

		if currWordWidth == 0 && currWordWidth + wordWidth < (hours - float64(i)) * 2.0 * tickWidth {
			context.SetRGB255(0, 0, 0)
			context.DrawStringAnchored(fmt.Sprintf("%02d", currTime.Hour()),
									   float64(2 * i) * tickWidth, 2.0 * float64(rowHeight) / 5.0, 0, 0.5)
			currWordWidth += wordWidth
		}
		currWordWidth -= 2.0 * tickWidth
		if currWordWidth < 0 {
			currWordWidth = 0
		}
		currTime = currTime.Add(time.Hour)
	}

	return context.Image()
}

func GetGraphRow(rowHeight, rowWidth int,
				 personSpans []types.TimeSpan,
				 totalSpan types.TimeSpan) (c color.Color, img image.Image) {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))

	startTime := totalSpan.StartTime.Truncate(time.Hour)
	endTime := totalSpan.EndTime.Truncate(time.Hour)
	if !endTime.Equal(totalSpan.EndTime) {
		endTime = endTime.Add(time.Hour)
	}

	hours := endTime.Sub(startTime).Truncate(time.Hour).Hours()
	tickWidth := float64(rowWidth) / 2.0 / hours
	c = color.RGBA{ R: uint8(random.Intn(0x100)), G: uint8(random.Intn(0x100)),
					B: uint8(random.Intn(0x100)), A: 0xFF }
	context := gg.NewContext(rowWidth, rowHeight)
	context.SetRGBA255(0xFF, 0xFF, 0xFF, 0)
	context.Clear()

	context.SetLineCapRound()
	context.SetDash(3.0, 3.0)
	for i := 0; i <= 2 * int(hours); i++ {
		if i % 2 == 0 {
			context.SetLineWidth(2.0)
			context.SetRGB255(0, 0, 0)
			context.DrawLine(float64(i) * tickWidth + 1, 0,
			 				 float64(i) * tickWidth + 1, float64(rowHeight))
		} else {
			context.SetLineWidth(1.0)
			context.SetRGB255(0xB0, 0xB0, 0xB0)
			context.DrawLine(float64(i) * tickWidth, 0,
				 			 float64(i) * tickWidth, float64(rowHeight))
		}
		context.Stroke()
	}

	context.SetColor(c)
	var spanStart, spanLength float64
	for _, span := range personSpans {
		spanStart = span.StartTime.Sub(startTime).Hours() * 2.0 * tickWidth
		spanLength = span.EndTime.Sub(span.StartTime).Hours() * 2.0 * tickWidth

		context.DrawRectangle(spanStart, 2 * float64(rowHeight) / 5.0, spanLength, float64(rowHeight) / 5.0)
		context.Fill()
	}

	return c, context.Image()
}

func GetMinuteRule (rowHeight, rowWidth int) (img image.Image) {
	gofont, _ := truetype.Parse(goregular.TTF)
	face := truetype.NewFace(gofont, &truetype.Options{Size: 2.0 * float64(rowHeight) / 5.0})

	tickWidth := float64(rowWidth) / 6.0
	wordWidth := float64(font.MeasureString(face, "00").Ceil())

	context := gg.NewContext(rowWidth, rowHeight)
	context.SetRGBA255(0xFF, 0xFF, 0xFF, 0)
	context.Clear()

	context.SetFontFace(face)

	context.SetLineWidth(1.0)
	context.SetLineCapRound()
	currTime := 0
	currWordWidth := 0.0

	for i := 0; i <= 6; i++ {
		context.SetRGB255(0xB0, 0xB0, 0xB0)
		context.DrawLine(float64(i) * tickWidth, 4.0 * float64(rowHeight) / 5.0,
						 float64(i) * tickWidth, float64(rowHeight))
		context.Stroke()

		if currWordWidth == 0 && currWordWidth + wordWidth < (6 - float64(i)) * tickWidth {
			context.SetRGB255(0, 0, 0)
			context.DrawStringAnchored(fmt.Sprintf("%02d", currTime),
									   float64(i) * tickWidth, 2.0 * float64(rowHeight) / 5.0, 0, 0.5)
			currWordWidth += wordWidth
		}
		currWordWidth -= tickWidth
		if currWordWidth < 0 {
			currWordWidth = 0
		}
		currTime += 10
	}

	return context.Image()
}

func GetHourGraphRows(rowHeight, rowWidth int,
					  storage types.DutyStorage) (images map[time.Time]map[int]image.Image, err error) {
	random := rand.New(rand.NewSource(time.Now().UnixNano()))

	var totalSpan types.TimeSpan
	if totalSpan, err = storage.TotalSpan(); err != nil {
		return nil, err
	}
	startTime, endTime := totalSpan.StartTime, totalSpan.EndTime
	startTime = startTime.Truncate(time.Hour)
	endTime = endTime.Truncate(time.Hour)
	if !endTime.Equal(totalSpan.EndTime) {
		endTime = endTime.Add(time.Hour)
	}

	images = make(map[time.Time]map[int]image.Image)
	personColors := make(map[int]color.Color)
	tickWidth := float64(rowWidth) / 6.0
	context := gg.NewContext(rowWidth, rowHeight)

	for person := range storage {
		personColors[person] = color.RGBA{ R: uint8(random.Intn(0x100)), G: uint8(random.Intn(0x100)),
										   B: uint8(random.Intn(0x100)), A: 0xFF }
	}

	for currHour := startTime; currHour.Before(endTime); currHour = currHour.Add(time.Hour) {
		images[currHour] = make(map[int]image.Image)

		subStorage, _ := storage.SubStorage(types.TimeSpan {
			StartTime: currHour,
			EndTime:   currHour.Add(time.Hour),
		})

		for person, spans := range subStorage {
			context.SetRGBA255(0xFF, 0xFF, 0xFF, 0)
			context.Clear()

			context.SetLineCapRound()
			context.SetDash(3.0, 3.0)
			context.SetLineWidth(1.0)
			context.SetRGB255(0xB0, 0xB0, 0xB0)
			for i := 0; i <= 6; i++ {
				context.DrawLine(float64(i) * tickWidth, 0,
		 						 float64(i) * tickWidth, float64(rowHeight))
				context.Stroke()
			}

			context.SetColor(personColors[person])
			for _, span := range spans {
				spanStart := span.StartTime.Sub(currHour).Minutes() / float64(time.Hour / time.Minute) * float64(rowWidth)
				spanLength := span.EndTime.Sub(span.StartTime).Minutes() / float64(time.Hour / time.Minute) * float64(rowWidth)

				context.DrawRectangle(spanStart, 2 * float64(rowHeight) / 5.0, spanLength, float64(rowHeight) / 5.0)
				context.Fill()
			}

			img := image.NewRGBA(context.Image().Bounds())
			draw.Draw(img, img.Bounds(), context.Image(), context.Image().Bounds().Min, draw.Src)

			images[currHour][person] = img
		}
	}

	return images, nil
}