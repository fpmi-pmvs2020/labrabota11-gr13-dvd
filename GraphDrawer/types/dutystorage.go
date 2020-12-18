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
				StartTime: record.StartTime,
				EndTime:   record.EndTime,
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

func (ds DutyStorage) SubStorage(span TimeSpan) (subStorage DutyStorage, err error) {
	if len(ds) == 0 {
		return nil, errors.New("no records in the storage")
	}

	subStorage = make(DutyStorage)

	for person, spans := range ds {
		for _, subSpan := range spans {
			tmpSpan := TimeSpan {
				StartTime: time.Time{},
				EndTime:   time.Time{},
			}

			if !(subSpan.StartTime.After(span.EndTime) || subSpan.EndTime.Before(span.StartTime)) {
				if subSpan.StartTime.Before(span.StartTime) {
					tmpSpan.StartTime = span.StartTime
				} else {
					tmpSpan.StartTime = subSpan.StartTime
				}
				if subSpan.EndTime.After(span.EndTime) {
					tmpSpan.EndTime = span.EndTime
				} else {
					tmpSpan.EndTime = subSpan.EndTime
				}
			}

			if _, exists := subStorage[person]; !exists {
				subStorage[person] = make([]TimeSpan, 0)
			}
			subStorage[person] = append(subStorage[person], subSpan)
		}
	}

	return subStorage, nil
}