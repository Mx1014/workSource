package server

import (
	"container/list"
	"fmt"
	"math/rand"
	"time"
)

const (
	MESSAGE_SIZE       = 1024
	maxBusDuration     = time.Duration(5) * time.Minute
	expiredBusDuration = time.Duration(5) * time.Second
)

type MessageBus struct {
	connections map[*Connection]bool
	register    chan *Connection
	unregister  chan *Connection
	messages    chan SeqMessage
	responses   chan Message
	timer       *time.Timer
	nextTick    time.Time
	expiredList *list.List
	seqMap      *SeqMap
}

var bus = MessageBus{
	connections: make(map[*Connection]bool),
	register:    make(chan *Connection),
	unregister:  make(chan *Connection),
	messages:    make(chan SeqMessage, MESSAGE_SIZE),
	responses:   make(chan Message, MESSAGE_SIZE),
	expiredList: list.New(),
	seqMap:      NewSeqMap(MESSAGE_SIZE * 10),
}

func (bus *MessageBus) Run() {
	l := bus.expiredList
	bus.nextTick = time.Now().Add(maxBusDuration)
	bus.timer = time.NewTimer(maxBusDuration)
	tCh := make(chan bool)
	go func() {
		for range bus.timer.C {
			tCh <- true
		}
	}()

	for {
		select {
		case conn := <-bus.register:
			bus.connections[conn] = true
		case conn := <-bus.unregister:
			if _, ok := bus.connections[conn]; ok {
				delete(bus.connections, conn)
				close(conn.sendMsg)
			}
		case m := <-bus.messages:
			oldData := bus.seqMap.NewSeq(m)
			if oldData != nil {
				fmt.Printf("FIXME: oldData is not null\n")
				oldData.(SeqMessage).CloseChannel()
			}

			now := time.Now()
			m.SetExpired(now.Add(expiredBusDuration))
			m.SetEl(l.PushBack(m))

			if bus.nextTick.After(m.GetExpred()) {
				bus.nextTick = m.GetExpred()
				bus.timer.Reset(bus.nextTick.Sub(now))
			}

			//TODO if the len of connections is bigger then 2
			if len(bus.connections) <= 2 {
				for conn := range bus.connections {
					select {
					case conn.sendMsg <- m.GetData():
					default:
						close(conn.sendMsg)
						delete(bus.connections, conn)
					}
				}
			} else {
				a1, a2 := rand.Intn(len(bus.connections)), rand.Intn(len(bus.connections))
				cnt := 0
				for {
					if a1 != a2 || cnt > 100 {
						break
					}
					a2 = rand.Int() % 8
					cnt++
				}
				//fmt.Printf("a1=%d a2=%d len=%d\n", a1, a2, len(bus.connections))
				cnt = 0
				for conn := range bus.connections {
					if cnt == a1 || cnt == a2 {
						select {
						case conn.sendMsg <- m.GetData():
						default:
							close(conn.sendMsg)
							delete(bus.connections, conn)
						}
					}
					cnt++
				}
			}
		case resp := <-bus.responses:
			fmt.Printf("bus got resp seq %d\n", resp.RequestId)
			data := bus.seqMap.GetData(resp.RequestId)
			if data != nil {
				//Not close channel at ok
				bus.seqMap.DelSeq(resp.RequestId)
				seqMsg := data.(SeqMessage)

				l.Remove(seqMsg.GetEl())
				seqMsg.SetEl(nil)

				seqMsg.PutResp(resp.Payload)
			} else {
				fmt.Printf("data not find in seqmap\n")
			}
		case <-tCh:
			now := time.Now()
			for l.Len() > 0 {
				el := l.Front()
				obj := el.Value.(SeqMessage)
				if obj.GetExpred().Before(now.Add(1 * time.Microsecond)) {
					//Timeout, remove from list
					l.Remove(el)
					obj.SetEl(nil)

					//remove from seqmap
					bus.seqMap.DelSeq(obj.GetRequestId())

					//Close it
					obj.(SeqMessage).CloseChannel()
				} else {
					//Not timeout
					bus.nextTick = l.Front().Value.(SeqMessage).GetExpred()
					bus.timer.Reset(bus.nextTick.Sub(now))
					break
				}
			}

			if 0 == l.Len() {
				//log.Println("bus all zero")
				bus.nextTick = time.Now().Add(maxBusDuration)
				bus.timer.Reset(bus.nextTick.Sub(now))
			}
		}
	}
}
