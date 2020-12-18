package server

import (
	"GraphDrawer/drawer"
	"GraphDrawer/types"
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"image"
	"io/ioutil"
	"net/http"
	"sort"
	"time"
)

func getRequestBody(r *http.Request) (body types.RequestBody, err error) {
	var response []byte
	if response, err = ioutil.ReadAll(r.Body); err != nil {
		return types.RequestBody{}, err
	}

	str := string(response)
	fmt.Println(str)

	if err = json.NewDecoder(bytes.NewBuffer(response)).Decode(&body); err != nil {
		return types.RequestBody{}, err
	}

	if body.RowWidth <= 0 || body.RowHeight <= 0 {
		return types.RequestBody{}, errors.New("incorrect sizes of a row")
	}

	return body, nil
}

func GetPicturesHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}

	var body types.RequestBody
	var err error
	if body, err = getRequestBody(r); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		_, _ = w.Write([]byte(err.Error()))
		return
	}

	if len(body.Records) == 0 {
		w.WriteHeader(http.StatusNoContent)
		return
	}

	result := make([]types.ResultBodyElement, 0)
	var img image.Image
	dutyStorage := types.CreateStorage(body.Records)
	totalSpan, _ := dutyStorage.TotalSpan()
	dutyStorage[0] = nil
	for person, personSpans := range dutyStorage {
		if person == 0 {
			img = drawer.GetHourRow(body.RowHeight, body.RowWidth, totalSpan)
		} else {
			_, img = drawer.GetGraphRow(body.RowHeight, body.RowWidth, personSpans, totalSpan)
		}
		rbe := types.ResultBodyElement{PersonId: person}
		if err = rbe.SavePNGImage(img); err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
		result = append(result, rbe)
	}

	_ = json.NewEncoder(w).Encode(types.ResultBody{List: result})
}

func GetHourlyPicturesHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}

	var body types.RequestBody
	var err error
	if body, err = getRequestBody(r); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		_, _ = w.Write([]byte("Error: " + err.Error()))
		return
	}

	if len(body.Records) == 0 {
		w.WriteHeader(http.StatusNoContent)
		return
	}

	result := make([][]types.ResultBodyElement, 0)
	dutyStorage := types.CreateStorage(body.Records)

	hourlyResult, _ := drawer.GetHourGraphRows(body.RowHeight, body.RowWidth, dutyStorage)

	hours := make([]time.Time, 0, len(hourlyResult))
	for hour := range hourlyResult {
		hours = append(hours, hour)
	}

	sort.Slice(hours, func (i, j int) bool { return hours[i].Before(hours[j]) } )

	for idx, hour := range hours {
		result = append(result, make([]types.ResultBodyElement, 0))
		for id, img := range hourlyResult[hour] {
			rbe := types.ResultBodyElement{PersonId: id}
			if err = rbe.SavePNGImage(img); err != nil {
				w.WriteHeader(http.StatusInternalServerError)
				_, _ = w.Write([]byte("Error: " + err.Error()))
				return
			}
			result[idx] = append(result[idx], rbe)
		}
	}

	hrb := types.HourlyResultBody{TimeMap: result}
	img := drawer.GetMinuteRule(body.RowHeight, body.RowWidth)
	if hrb.MinuteRuleString, err = types.ConvertToPNG(img); err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		_, _ = w.Write([]byte("Error: " + err.Error()))
	}

	_ = json.NewEncoder(w).Encode(hrb)
}