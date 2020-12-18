package types

import "time"

type TimeSpan struct {
	StartTime time.Time
	EndTime   time.Time
}

type RawRecord struct {
	PersonId  int       `json:"personId"`
	StartTime time.Time `json:"from"`
	EndTime   time.Time `json:"to"`
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

type HourlyResultBody struct {
	MinuteRuleString string `json:"minuteRuleString"`
	TimeMap [][]ResultBodyElement `json:"timeMap"`
}