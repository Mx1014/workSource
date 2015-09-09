package main

import (
	"fmt"
	"math/rand"
	"time"

	"container/list"

	"github.com/ryszard/goskiplist/skiplist"
)

const (
	vulcanQuote = "Live long and prosper."
	size        = 10000
)

type Item struct {
	expired time.Time
	value   int
	el      *list.Element
}

var mylist *list.List
var mymap map[string]*Item

func main() {
	var orderMap *skiplist.SkipList
	orderMap = skiplist.NewCustomMap(func(l, r interface{}) bool {
		return l.(time.Time).Before(r.(time.Time))
	})

	a1, a2 := rand.Int()%8, rand.Int()%8
	cnt := 0
	for {
		if a1 != a2 || cnt > 100 {
			break
		}
		a2 = rand.Int() % 8
		cnt++
	}
	fmt.Printf("a1=%d, a2=%d\n", a1, a2)

	orderMap.Set(time.Now(), "abc")

	mylist = list.New()
	mymap = make(map[string]*Item)

	now := time.Now()
	for i := 1; i < 9; i++ {
		t := now.Add(time.Duration(i) * time.Second)
		item := &Item{t, i, nil}
		fmt.Println("the t is :", i, t)
		item.el = mylist.PushBack(item)
		mymap[fmt.Sprintf("aaa%d", i)] = item
	}

	item := mymap[fmt.Sprintf("aaa3")]
	mylist.Remove(item.el)
	item.el = mylist.PushBack(item)

	for i := 0; i < 10; i++ {
		time.Sleep(1 * time.Second)
		println("after 1s")
		do_expired(mylist)
	}
}

func do_expired(l *list.List) {
	for l.Len() > 0 {
		elem := l.Front()
		item := elem.Value.(*Item)
		now := time.Now()
		if item.expired.Before(now) {
			l.Remove(elem)
			fmt.Println("expired ", item.value)
		} else {
			break
		}
	}
}

func insertLinkList(l *list.List) {
	defer timeTrack(time.Now(), "insertLinkList")
	for i := 0; i < size; i++ {
		l.PushBack(vulcanQuote)
	}
}

func iterateLinkList(l *list.List) {
	defer timeTrack(time.Now(), "iterateLinkList")
	for e := l.Front(); e != nil; e = e.Next() {
		fmt.Sprintln(e.Value)
	}
}

func timeTrack(start time.Time, name string) {
	elapsed := time.Since(start)
	fmt.Printf("%s took %s\n", name, elapsed)
}
