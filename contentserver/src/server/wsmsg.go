package server

import (
	"container/list"
	"fmt"
	"time"
)

type WsMessage struct {
	Message
	el      *list.Element
	expired time.Time
	resp    chan string
}

func NewWsMessage(name string, payload string) *WsMessage {
	msg := &WsMessage{Message{rpcVersion, name, -1, rpcAppId, payload}, nil, time.Now(), make(chan string, 0)}
	return msg
}

func (wsMsg *WsMessage) PutResp(m string) {
	//TODO for the channel closed ???
	wsMsg.resp <- m
}

func (wsMsg *WsMessage) GetData() *Message {
	return &wsMsg.Message
}

func (wsMsg *WsMessage) CloseChannel() {
	wsMsg.resp <- "errorresp"
}

func (wsMsg *WsMessage) GetExpred() time.Time {
	return wsMsg.expired
}

func (wsMsg *WsMessage) SetExpired(expired time.Time) {
	wsMsg.expired = expired
}

func (wsMsg *WsMessage) SetEl(el *list.Element) {
	wsMsg.el = el
}

func (wsMsg *WsMessage) GetEl() *list.Element {
	return wsMsg.el
}

func (wsMsg *WsMessage) GetResp() (string, error) {
	bus.messages <- wsMsg

	respData := <-wsMsg.resp
	close(wsMsg.resp)

	if respData == "errorresp" {
		return "", fmt.Errorf("got msg timeout: %v", wsMsg)
	} else {
		return respData, nil
	}
}
