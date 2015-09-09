package main

import (
	"fmt"
	"path/filepath"

	"github.com/peterbourgon/diskv"
)

func blockTransform(s string) []string {
	block := 2
	word := 2
	pathSlice := make([]string, block)
	if len(s) < block*word {
		for i := 0; i < block; i++ {
			pathSlice[i] = "__small"
		}
		return pathSlice
	}

	for i := 0; i < block; i++ {
		pathSlice[i] = s[word*i : word*(i+1)]
	}
	return pathSlice
}

func pathFor(d *diskv.Diskv, key string) string {
	return filepath.Join(d.BasePath, filepath.Join(d.Transform(key)...))
}

func main() {
	// Initialize a new diskv store, rooted at "my-data-dir", with a 1MB cache.
	d := diskv.New(diskv.Options{
		BasePath:     "/tmp/my-data-dir",
		Transform:    blockTransform,
		CacheSizeMax: 1024 * 1024,
	})

	// Write three bytes to the key "alpha".
	key := "alpha"
	d.Write(key, []byte{'1', '2', '5'})

	key = "ablfjfja"
	d.Write(key, []byte{'1', '2', '5'})

	key = "ablfjddf"
	d.Write(key, []byte{'1', '2', '5'})

	// Read the value back out of the store.
	value, _ := d.Read(key)
	fmt.Printf("%v\n", value)

	// Erase the key+value from the store (and the disk).
	//d.Erase(key)
}
