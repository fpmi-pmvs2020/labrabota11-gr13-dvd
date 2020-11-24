package types

import "time"

type TimeSpan struct {
	StartTime time.Time
	EndTime   time.Time
}

type RawDate struct {
	Day   int `json:"day"`
	Month int `json:"month"`
	Year  int `json:"year"`
}

type RawTime struct {
	Hour   int `json:"hour"`
	Minute int `json:"minute"`
	Second int `json:"second"`
}

type RawDateTime struct {
	Date RawDate `json:"date"`
	Time RawTime `json:"time"`
}

type RawRecord struct {
	PersonId  int         `json:"personId"`
	StartTime RawDateTime `json:"from"`
	EndTime   RawDateTime `json:"to"`
}

type DutyStorage map[int][]TimeSpan

type RequestBody struct {
	RowWidth  int		  `json:"rowWidth"`
	RowHeight int		  `json:"rowHeight"`
	Records   []RawRecord `json:"records"`
}

type ResultBodyElement struct {
	PersonId   int    `json:"personId"`
	FileString string `json:"fileString"`
}

type ResultBody struct {
	List []ResultBodyElement `json:"list"`
}