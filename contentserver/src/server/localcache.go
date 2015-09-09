package server

import (
	"bufio"
	"bytes"
	"container/list"
	"encoding/json"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os/exec"
	"path/filepath"
	"time"

	"github.com/peterbourgon/diskv"
)

const (
	maxDuration     = time.Duration(15) * time.Minute
	expiredDuration = time.Duration(10) * time.Second
)

type ChunkObject struct {
	Md5   string
	index int
	data  []byte
	resp  chan *ObjectInfoResp
}

type LocalObject struct {
	item       *ObjectInfo
	el         *list.Element
	expired    time.Time
	chunkLen   int
	waitChunks []bool
	processing bool
	rv         chan int
}

type LocalCache struct {
	diskv      *diskv.Diskv
	expireList *list.List
	objectMap  map[string]*LocalObject
	timer      *time.Timer
	nextTick   time.Time
	newCh      chan *LocalObject
	completeCh chan *LocalObject
	chunkCh    chan *ChunkObject
}

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

func NewChunkObject(md5 string, index int, data []byte) *ChunkObject {
	return &ChunkObject{md5, index, data, make(chan *ObjectInfoResp)}
}

func NewLocalObject(item *ObjectInfo) *LocalObject {
	if item == nil || item.ChunkSize < 1024 || item.TotalSize < 0 {
		return nil
	}

	n := item.TotalSize / item.ChunkSize
	if n == 0 {
		n = 1
	}
	return &LocalObject{item, nil, time.Now().Add(expiredDuration), n, make([]bool, n), false, make(chan int)}
}

func (obj *LocalObject) EraseMyself(cache *LocalCache) {
	delete(cache.objectMap, obj.item.Md5)
	for i := 0; i < obj.chunkLen; i++ {
		if obj.waitChunks[i] {
			key := fmt.Sprintf(obj.item.Md5+".%d", i)
			cache.diskv.Erase(key)
		}
	}
	cache.diskv.Erase(obj.item.Md5)

	obj.el = nil

	//close channel hear
	close(obj.rv)
}

func NewLocalCache(path string) *LocalCache {
	var cache *LocalCache
	cache = new(LocalCache)
	cache.diskv = diskv.New(diskv.Options{
		BasePath:     path,
		Transform:    blockTransform,
		CacheSizeMax: 1024 * 1024, // 1MB
	})
	cache.expireList = list.New()
	cache.objectMap = make(map[string]*LocalObject)
	cache.newCh = make(chan *LocalObject, 1000)
	cache.completeCh = make(chan *LocalObject, 1000)
	cache.chunkCh = make(chan *ChunkObject, 1000)

	return cache
}

func (cache *LocalCache) PathFor(key string) string {
	return filepath.Join(cache.diskv.BasePath, filepath.Join(cache.diskv.Transform(key)...), key)
}

func (cache *LocalCache) newObjEvent(obj *LocalObject) {
	//If the object is processing, wait
	if _, ok := cache.objectMap[obj.item.Md5]; ok {
		obj.rv <- 1
		return
	}
	cache.objectMap[obj.item.Md5] = obj

	// The newer expired object always be biggest and added to the end of list
	now := time.Now()
	obj.expired = now.Add(expiredDuration)
	obj.el = cache.expireList.PushBack(obj)

	//update the next tick and reset the timer
	if cache.nextTick.After(obj.expired) {
		cache.nextTick = obj.expired
		cache.timer.Reset(cache.nextTick.Sub(now))
	}

	//added ok
	obj.rv <- 0
}

func audio_info(filePath string) (interface{}, error) {
	ffprobe := exec.Command("ffprobe", "-v", "quiet", "-print_format", "json", "-show_format", filePath)
	stdin, _ := ffprobe.StdinPipe()
	stdout, _ := ffprobe.StdoutPipe()
	if err := ffprobe.Start(); err != nil {
		return nil, fmt.Errorf("exec error, %v\n", err)
	}

	stdin.Close()
	data2, _ := ioutil.ReadAll(stdout)

	done := make(chan error, 1)
	go func() {
		done <- ffprobe.Wait()
	}()
	select {
	case <-time.After(3 * time.Second):
		if err := ffprobe.Process.Kill(); err != nil {
			log.Fatal("failed to kill: ", err)
		}
		<-done // allow goroutine to exit
		return nil, fmt.Errorf("process killed")
	case err := <-done:
		if err != nil {
			return nil, err
		}
	}

	if len(data2) == 0 {
		return nil, fmt.Errorf("not output")
	} else {
		var result interface{}
		//TODO how to check error hear
		err := json.Unmarshal(data2, &result)
		if err != nil {
			return nil, err
		}
		return result, nil
	}
}

func (cache *LocalCache) newChunkEvent(httpd *ServerHttpd, chunk *ChunkObject) {
	obj, ok := cache.objectMap[chunk.Md5]
	if !ok {
		//TODO use const for rv code
		chunk.resp <- &ObjectInfoResp{-1, "md5 not found", "", ""}
		return
	}

	if obj.processing {
		chunk.resp <- &ObjectInfoResp{0, "processing", "", ""}
		return
	}

	//TODO do better, check more.
	if chunk.index < 0 || chunk.index >= obj.chunkLen || len(chunk.data) <= 0 || len(chunk.data) > 2*obj.item.ChunkSize {
		chunk.resp <- &ObjectInfoResp{-2, "params error", "", ""}
		return
	}

	// remove from expiredList first
	cache.expireList.Remove(obj.el)
	obj.el = nil

	cache.diskv.Write(fmt.Sprintf("%s.%d", obj.item.Md5, chunk.index), chunk.data)
	obj.waitChunks[chunk.index] = true

	b := true
	for i := 0; i < obj.chunkLen; i++ {
		if !obj.waitChunks[i] {
			b = false
			break
		}
	}
	if b {
		// Now ignore the other's data and move local cache to global storage
		obj.processing = true

		go func() {
			complete := true
			buf := new(bytes.Buffer)
			writer := bufio.NewWriter(buf)
			for i := 0; i < obj.chunkLen; i++ {
				key := fmt.Sprintf("%s.%d", obj.item.Md5, i)
				v, err := cache.diskv.Read(key)
				if err != nil {
					complete = false
					break
				}
				io.Copy(writer, bytes.NewReader(v))
			}

			if !complete {
				chunk.resp <- &ObjectInfoResp{-3, "got all block but read from local error", "", ""}
				cache.completeCh <- obj
				return
			}

			writer.Flush()

			buf_bytes := buf.Bytes()
			if obj.item.ObjType == "audio" {
				cache.diskv.Write(obj.item.Md5, buf_bytes)
				fmt.Printf("begin audio info\n")
				info, err := audio_info(cache.PathFor(obj.item.Md5))
				if err != nil {
					fmt.Printf("error: %v\n", err.Error())
				} else if info != nil {
					map_info := info.(map[string]interface{})
					if duration, ok := map_info["format"]; ok {
						duration_map := duration.(map[string]interface{})
						if nil != duration_map["duration"] {
							obj.item.Meta["duration"] = duration_map["duration"].(string)
						}
					}
				}
			}

			//TODO do better for httpd
			rv, uri, url := saveObjectInfo(httpd, obj.item, buf_bytes)
			if rv {
				chunk.resp <- &ObjectInfoResp{1, "", uri, url}
			} else {
				chunk.resp <- &ObjectInfoResp{-3, "got all block but stream is error", "", ""}
			}

			cache.completeCh <- obj
		}()

	} else {
		//reset the expired
		now := time.Now()
		obj.expired = now.Add(expiredDuration)
		obj.el = cache.expireList.PushBack(obj)
		if cache.nextTick.After(obj.expired) {
			cache.nextTick = obj.expired
			cache.timer.Reset(cache.nextTick.Sub(now))
		}

		chunk.resp <- &ObjectInfoResp{0, "wait for continue block", "", ""}
	}

}

func (cache *LocalCache) PutObjectInfo(obj *ObjectInfo) int {
	localObj := NewLocalObject(obj)
	if localObj == nil {
		return -1
	}

	cache.newCh <- localObj
	rv := <-localObj.rv

	//new obj error, we close the rv channel hear
	if rv < 0 {
		close(localObj.rv)
	}

	return rv
}

func (cache *LocalCache) PutChunking(md5 string, index int, data []byte) *ObjectInfoResp {
	chunk := NewChunkObject(md5, index, data)
	cache.chunkCh <- chunk

	objResp := <-chunk.resp
	close(chunk.resp)
	return objResp
}

func (cache *LocalCache) run(httpd *ServerHttpd) {
	l := cache.expireList

	// The simple timeout management
	cache.nextTick = time.Now().Add(maxDuration)
	cache.timer = time.NewTimer(maxDuration)
	tCh := make(chan bool)

	go func() {
		for range cache.timer.C {
			tCh <- true
			//fmt.Println("Tick at", t)
		}
	}()

	for {
		select {
		case newObj := <-cache.newCh:
			httpd.context.Logger.Info("got new objectInfo %v", newObj)
			cache.newObjEvent(newObj)

		case completeObj := <-cache.completeCh:
			httpd.context.Logger.Info("got a complete object info")
			completeObj.EraseMyself(cache)

		case chunk := <-cache.chunkCh:
			//httpd.context.Logger.Info("got new chunk %v", chunk)
			cache.newChunkEvent(httpd, chunk)

		case <-tCh:
			now := time.Now()
			for l.Len() > 0 {
				el := l.Front()
				obj := el.Value.(*LocalObject)
				if obj.expired.Before(now.Add(1 * time.Microsecond)) {
					httpd.context.Logger.Info("got a timeout %v", obj)
					l.Remove(el)
					obj.el = nil
					obj.EraseMyself(cache)
				} else {
					httpd.context.Logger.Info("not timeout")
					cache.nextTick = l.Front().Value.(*LocalObject).expired
					cache.timer.Reset(cache.nextTick.Sub(now))
					break
				}
			}
			if l.Len() == 0 {
				httpd.context.Logger.Info("all zero")
				cache.nextTick = time.Now().Add(maxDuration)
				cache.timer.Reset(cache.nextTick.Sub(now))
			}
		}
	}
}
