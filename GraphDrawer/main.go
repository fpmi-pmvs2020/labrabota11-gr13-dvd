package main

import (
	"GraphDrawer/server"
	"bufio"
	"log"
	"net/http"
	"os"
)

func main() {
	http.HandleFunc("/stripes", server.GetPicturesHandler)
	http.HandleFunc("/stripesHourly", server.GetHourlyPicturesHandler)
	go func() {
		log.Fatal(http.ListenAndServeTLS(":8080", "server.crt", "server.key", nil))
	}()
	go func() {
		log.Fatal(http.ListenAndServe(":8081", nil))
	}()

	reader := bufio.NewReader(os.Stdin)
	_, _, _ = reader.ReadRune()
}
