package server

import (
	"GraphDrawer/drawer"
	"GraphDrawer/types"
	"encoding/json"
	"image"
	"net/http"
)

func GetPicturesHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}

	var body types.RequestBody
	var err error
	if err = json.NewDecoder(r.Body).Decode(&body); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	if body.RowWidth <= 0 || body.RowHeight <= 0 {
		w.WriteHeader(http.StatusBadRequest)
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
