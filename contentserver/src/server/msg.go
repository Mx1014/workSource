package server

import (
	"container/list"
	"encoding/binary"
	"errors"
	"fmt"
	"time"

	"github.com/golang/protobuf/proto"
)

const (
	META_HEADER_LEN = 32
	MAGIC           = 0x43329810
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

type MetaHeader struct {
	magic     int
	metaLen   int
	fragSize  int
	totalSize int
}

func (seqData *Message) GetRequestId() int {
	return seqData.RequestId
}

func (seqData *Message) SetRequestId(requestId int) {
	seqData.RequestId = requestId
}

func metaToData(meta map[string]string) ([]byte, error) {
	metaMsg := &DataMeta{Item: make([]*MetaItem, 0)}
	for k, v := range meta {
		metaMsg.Item = append(metaMsg.Item, &MetaItem{Key: &k, Value: &v})
	}

	return proto.Marshal(metaMsg)
}

func fileMetaCreate(fragSize int, total int, meta map[string]string) ([]byte, error) {
	//MAGIC(8) + metaLen(4) + fragSize(4) + total(8) + notUsed(8)
	header := make([]byte, META_HEADER_LEN)
	metaData, err := metaToData(meta)
	if err != nil {
		return nil, err
	}

	//fmt.Printf("metadata len=%d\n", len(metaData))

	s := 0
	binary.BigEndian.PutUint64(header[s:s+8], MAGIC)
	s += 8
	binary.BigEndian.PutUint32(header[s:s+4], uint32(len(metaData)))
	s += 4
	binary.BigEndian.PutUint32(header[s:s+4], uint32(fragSize))
	s += 4
	binary.BigEndian.PutUint64(header[s:s+8], uint64(total))
	s += 8

	header = append(header, metaData...)
	return header, nil
}

func fileMetaGet(data []byte) (header *MetaHeader, meta map[string]string, err error) {
	header = &MetaHeader{}
	s := 0
	header.magic = int(binary.BigEndian.Uint64(data[s : s+8]))
	s += 8
	if header.magic != MAGIC {
		return nil, nil, errors.New("magic error")
	}

	header.metaLen = int(binary.BigEndian.Uint32(data[s : s+4]))
	s += 4
	header.fragSize = int(binary.BigEndian.Uint32(data[s : s+4]))
	s += 4
	header.totalSize = int(binary.BigEndian.Uint64(data[s : s+8]))
	s += 8

	if header.fragSize == header.totalSize && (len(data)-META_HEADER_LEN-header.metaLen) != header.totalSize {
		return nil, nil, errors.New("fileMeta total size error")
	} else if header.fragSize > header.totalSize {
		return nil, nil, errors.New("fileMeta total size error")
	}

	if (META_HEADER_LEN + header.metaLen) > len(data) {
		return nil, nil, errors.New("fileMeta error for metaLen")
	}

	//fmt.Printf("header: %v\n", header)
	metaObj := &DataMeta{}
	err = proto.Unmarshal(data[META_HEADER_LEN:META_HEADER_LEN+header.metaLen], metaObj)
	if err != nil {
		return nil, nil, err
	}

	fmt.Printf("meta object got=%v\n", metaObj)

	meta = make(map[string]string)
	for _, item := range metaObj.GetItem() {
		//fmt.Printf("key=%v value=%v", *item.Key, *item.Value)
		meta[*item.Key] = *item.Value
	}

	return
}
