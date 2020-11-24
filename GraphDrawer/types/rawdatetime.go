package types

import "time"

func (rdt *RawDateTime) ToTime() (goTime time.Time) {
	goTime = time.Date(rdt.Date.Year, time.Month(rdt.Date.Month), rdt.Date.Day,
					   rdt.Time.Hour, rdt.Time.Minute, rdt.Time.Second,
					   0, time.Local)
	return goTime
}
