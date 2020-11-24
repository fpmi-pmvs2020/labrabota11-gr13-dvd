package types

import (
	"errors"
	"time"
)

func CreateStorage(records []RawRecord) (storage DutyStorage) {
	storage = make(DutyStorage)

	for _, record := range records {
		if _, ok := storage[record.PersonId]; !ok {
			storage[record.PersonId] = make([]TimeSpan, 0)
		}

		storage[record.PersonId] =
		 	append(storage[record.PersonId], TimeSpan {
				StartTime: record.StartTime.ToTime(),
				EndTime:   record.EndTime.ToTime(),
			})
	}

	return storage
}

func (ds DutyStorage) TotalSpan() (totalSpan TimeSpan, err error) {
	if len(ds) == 0 {
		return TimeSpan{}, errors.New("no records in the storage")
	}

	totalSpan = TimeSpan{
		StartTime: time.Time{},
		EndTime:   time.Time{},
	}

	for _, spans := range ds {
		for _, span := range spans {
			if totalSpan.StartTime.IsZero() || totalSpan.StartTime.After(span.StartTime) {
				totalSpan.StartTime = span.StartTime
			}
			if totalSpan.EndTime.IsZero() || totalSpan.EndTime.Before(span.EndTime) {
				totalSpan.EndTime = span.EndTime
			}
		}
	}

	return totalSpan, nil
}