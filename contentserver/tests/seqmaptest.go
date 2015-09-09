package main

import "fmt"

const (
	SEQ_START = 0
)

type SeqData interface {
	GetRequestId() int
	SetRequestId(seq int)
}

type Message struct {
	Name      string `json:"name"`
	RequestId int    `json:"requestId"`
}

func (seqData *Message) GetRequestId() int {
	return seqData.RequestId
}

func (seqData *Message) SetRequestId(requestId int) {
	seqData.RequestId = requestId
}

type SeqMap struct {
	index2obj []SeqData
	seq2index []int
	maxSize   int
	pos       int
	curr_seq  int
}

func NewSeqMap(maxSize int) *SeqMap {
	seqMap := &SeqMap{index2obj: make([]SeqData, maxSize), seq2index: make([]int, maxSize), maxSize: maxSize, pos: 0, curr_seq: 0}
	for i := 0; i < maxSize; i++ {
		seqMap.seq2index[i] = -1
		seqMap.index2obj[i] = nil
	}

	return seqMap
}

func (seqMap *SeqMap) NewSeq(data SeqData) SeqData {
	seq := seqMap.curr_seq
	if seq >= seqMap.maxSize {
		seq = SEQ_START
	}

	if seqMap.pos >= seqMap.maxSize {
		panic("exceed pos size")
	}

	index := seqMap.seq2index[seq]
	var oldData SeqData
	if index >= 0 {
		oldData = seqMap.index2obj[index]
	}

	data.SetRequestId(seq)
	seqMap.seq2index[seq] = seqMap.pos
	seqMap.index2obj[seqMap.pos] = data
	seqMap.curr_seq = seq + 1
	seqMap.pos += 1

	return oldData
}

func (seqMap *SeqMap) DelSeq(seq int) SeqData {
	if seq < SEQ_START || seq >= seqMap.maxSize {
		return nil
	}

	index := seqMap.seq2index[seq]
	if index < 0 {
		return nil
	}
	data := seqMap.index2obj[index]
	if data == nil || data.GetRequestId() != seq {
		return nil
	}

	seqMap.pos -= 1
	if seqMap.pos < 0 {
		panic("too small for pos size")
	}

	if index == seqMap.pos {
		seqMap.index2obj[index] = nil
	} else {
		//Set last pos to current deleted pos
		seqMap.index2obj[index] = seqMap.index2obj[seqMap.pos]
		seqMap.seq2index[seqMap.index2obj[seqMap.pos].GetRequestId()] = index
		seqMap.index2obj[seqMap.pos] = nil
	}

	return data
}

func (seqMap *SeqMap) GetData(seq int) SeqData {
	if seq < SEQ_START || seq >= seqMap.maxSize {
		return nil
	}

	index := seqMap.seq2index[seq]
	if index < 0 {
		return nil
	}
	data := seqMap.index2obj[index]
	if data == nil || data.GetRequestId() != seq {
		return nil
	}

	return data
}

func main() {
	seqMap := NewSeqMap(4)

	data1 := &Message{"a", 0}
	data2 := &Message{"a", 0}
	data3 := &Message{"a", 0}
	data4 := &Message{"a", 0}
	data5 := &Message{"a", 0}

	oldData := seqMap.NewSeq(data1)
	fmt.Printf("oldData:%v data1:%v\n", oldData, data1)

	oldData = seqMap.NewSeq(data2)
	fmt.Printf("oldData:%v data2:%v\n", oldData, data2)

	oldData = seqMap.NewSeq(data3)
	fmt.Printf("oldData:%v data3:%v\n", oldData, data3)

	oldData = seqMap.NewSeq(data4)
	fmt.Printf("oldData:%v data4:%v\n", oldData, data4)

	oldData = seqMap.DelSeq(2)
	fmt.Printf("%v %v %v\n", seqMap.GetData(1), seqMap.GetData(2), seqMap.GetData(3))

	oldData = seqMap.NewSeq(data5)
	fmt.Printf("oldData:%v data5:%v\n", oldData, data1)

	fmt.Printf("%v %v %v\n", seqMap.GetData(1), seqMap.GetData(2), seqMap.GetData(3))

	oldData = seqMap.DelSeq(2)
	fmt.Printf("%v %v %v\n", seqMap.GetData(1), seqMap.GetData(2), seqMap.GetData(3))
}
