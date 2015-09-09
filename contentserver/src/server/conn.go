package server

import (
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"

	"github.com/gorilla/websocket"
)

const (
	writeWait = 10 * time.Second

	pongWait = 60 * time.Second

	pingPeriod = (pongWait * 9) / 10

	maxMessageSize = 4096

	rpcPrefix         = "contentstorage"
	rpcResponsePrefix = "contentstorage.response"
	rpcRequestPrefix  = "contentstorage.request"
)

var upgrader = websocket.Upgrader{
	ReadBufferSize:  4096,
	WriteBufferSize: 4096,
}

type Connection struct {
	ws *websocket.Conn

	sendMsg chan *Message
}

func (c *Connection) readPump(s *ServerHttpd) {
	defer func() {
		bus.unregister <- c
		c.ws.Close()
	}()
	c.ws.SetReadLimit(maxMessageSize)
	c.ws.SetReadDeadline(time.Now().Add(pongWait))
	c.ws.SetPongHandler(func(string) error {
		c.ws.SetReadDeadline(time.Now().Add(pongWait))
		//s.context.Logger.Debug("websocket got pong")
		return nil
	})

	for {
		var msg Message
		err := c.ws.ReadJSON(&msg)
		if err != nil {
			fmt.Printf("parsed message error %v\n", err)
			break
		} else {
			fmt.Printf("parsed message: seq:%d name:%s %v\n", msg.RequestId, msg.Name, msg)
			//TODO for request
			if strings.HasPrefix(strings.ToLower(msg.Name), rpcResponsePrefix) {
				bus.responses <- msg
			} else {
				ParseRequest(s, c, &msg)
			}
		}
	}
}

func (c *Connection) write(mt int, payload []byte) error {
	c.ws.SetWriteDeadline(time.Now().Add(writeWait))
	return c.ws.WriteMessage(mt, payload)
}

func (c *Connection) writeJSON(msg interface{}) error {
	c.ws.SetWriteDeadline(time.Now().Add(writeWait))
	return c.ws.WriteJSON(msg)
}

func (c *Connection) writePump(s *ServerHttpd) {
	ticker := time.NewTicker(pingPeriod)
	defer func() {
		ticker.Stop()
		c.ws.Close()
	}()
	for {
		select {
		case msg, ok := <-c.sendMsg:
			if !ok {
				c.write(websocket.CloseMessage, []byte{})
				s.context.Logger.Debug("websocket message error")
				return
			}
			//Fix msg name.
			msg.Name = rpcPrefix + "." + msg.Name
			if err := c.writeJSON(msg); err != nil {
				s.context.Logger.Debug("websocket closed because write error %v err:%v", msg, err)
				return
			}
		case <-ticker.C:
			//s.context.Logger.Info("websocket closed because of ping timeout")
			if err := c.write(websocket.PingMessage, []byte{}); err != nil {
				//if error, return
				s.context.Logger.Debug("websocket closed because of ping error")
				return
			}
		}
	}
}

// serverWs handles websocket requests from the peer.
func serveWs(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	//TODO check hear
	/* if r.Method != "GET" {
		return 200, fmt.Errorf("Method not allowed")
	} */
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
		return 404, fmt.Errorf("Upgrade error")
	}
	s.context.Logger.Info("Got new connection\n")
	c := &Connection{sendMsg: make(chan *Message, 256), ws: ws}
	bus.register <- c
	go c.writePump(s)

	//Websocket run forever hear
	c.readPump(s)

	s.context.Logger.Info("endof serveWs")
	return 200, nil
}
