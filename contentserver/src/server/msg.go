package server

import (
	"container/list"
	"time"
)

type Message struct {
	Version   string `json:"version"`
	Name      string `json:"name"`
	RequestId int    `json:"requestId"`
	AppId     int    `json:"appId"`
	Payload   string `json:"payload"`
}

type SeqMessage interface {
	GetRequestId() int
	SetRequestId(requestId int)
	PutResp(m string)
	GetData() *Message
	CloseChannel()
	SetExpired(expired time.Time)
	GetExpred() time.Time
	SetEl(*list.Element)
	GetEl() *list.Element
}

func (seqData *Message) GetRequestId() int {
	return seqData.RequestId
}

func (seqData *Message) SetRequestId(requestId int) {
	seqData.RequestId = requestId
}
