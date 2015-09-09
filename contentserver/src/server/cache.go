package server

import (
	"fmt"
	"time"

	"github.com/bradfitz/gomemcache/memcache"
)

//TODO use a long connection or connection poll ?
//https://groups.google.com/forum/#!topic/golang-nuts/fugquVLZh4Q
//https://github.com/youtube/vitess/blob/master/go/memcache/memcache.go
//"github.com/bradfitz/gomemcache/memcache"
//"gopkgs.com/memcache.v2"
type ServerCache struct {
	host string
	port int

	//Not long connection version
	client *memcache.Client
}

func NewCache(h string, p int) *ServerCache {
	cacheAddr := fmt.Sprintf("%s:%d", h, p)
	c := memcache.New(cacheAddr)
	//TODO use config for timeout
	c.Timeout = 1000 * time.Millisecond
	return &ServerCache{client: c, host: h, port: p}
}

func (scache *ServerCache) Retry() {
	cacheAddr := fmt.Sprintf("%s:%d", scache.host, scache.port)
	c := memcache.New(cacheAddr)
	scache.client = c
}

func (scache *ServerCache) Get(key string) (string, error) {
	it, err := scache.client.Get(key)
	if err != nil {
		return "", err
	}
	return string(it.Value), nil
}

func (scache *ServerCache) Set(k string, v string) error {
	it := &memcache.Item{Key: k, Value: []byte(v)}
	return scache.client.Set(it)
}

func (scache *ServerCache) Exist(key string) bool {
	fmt.Printf("Exist key=%v\n", key)
	_, err := scache.Get(key)
	if err != nil {
		return false
	}
	return true
}

func (scache *ServerCache) GetBytes(key string) ([]byte, error) {
	it, err := scache.client.Get(key)
	if err != nil {
		fmt.Println(err)
		return nil, err
	} else {
		return it.Value, nil
	}

}

func (scache *ServerCache) SetBytes(k string, v []byte) error {
	fmt.Printf("SetBytes key=%v\n", k)
	it := &memcache.Item{Key: k, Value: v}
	return scache.client.Set(it)
}

func (scache *ServerCache) Del(key string) error {
	return scache.client.Delete(key)
}
