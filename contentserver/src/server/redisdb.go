package server

import (
	"errors"
	"fmt"
	"time"

	"github.com/garyburd/redigo/redis"
)

const (
	contentHSet = "contentserver"
)

type RedisDB struct {
	server    string
	port      int
	pool      *redis.Pool
	isConnect bool
}

func NewRedisDB(s string, p int) (*RedisDB, error) {
	addr := fmt.Sprintf("%s:%d", s, p)
	pool := &redis.Pool{
		MaxIdle:     3,
		IdleTimeout: 240 * time.Second,
		Dial: func() (redis.Conn, error) {
			c, err := redis.Dial("tcp", addr)
			if err != nil {
				return nil, err
			}

			return c, err
		},
		TestOnBorrow: func(c redis.Conn, t time.Time) error {
			_, err := c.Do("PING")
			return err
		},
	}

	return &RedisDB{
		server:    s,
		port:      p,
		pool:      pool,
		isConnect: true,
	}, nil
}

func (z *RedisDB) Retry() {
}

func (z *RedisDB) getConnect() (redis.Conn, error) {
	if z.isConnect {
		conn := z.pool.Get()
		return conn, nil
	} else {
		return nil, errors.New("Can not connect db")
	}
}

func (z *RedisDB) Exist(key string) bool {
	conn, err := z.getConnect()
	if err != nil {
		return false
	}
	defer conn.Close()

	isExists, _ := redis.Bool(conn.Do("HEXISTS", contentHSet, key))
	return isExists
}

func (z *RedisDB) GetBytes(key string) ([]byte, error) {
	conn, err := z.getConnect()
	if err != nil {
		return nil, errors.New("Can not connect db!")
	}
	defer conn.Close()

	data, err := redis.Bytes(conn.Do("HGET", contentHSet, key))
	if err != nil {
		return nil, err
	}
	return data, nil
}

func (z *RedisDB) SetBytes(key string, v []byte) error {
	return z.Send("HSET", contentHSet, key, v)
}

func (z *RedisDB) Del(key string) error {
	return z.Send("HDEL", contentHSet, key)
}

func (z *RedisDB) Do(commandName string, args ...interface{}) (interface{}, error) {
	conn, err := z.getConnect()

	if err != nil {
		return nil, errors.New("Can not connect db!")
	}
	defer conn.Close()

	return conn.Do(commandName, args...)
}

func (z *RedisDB) Send(commandName string, args ...interface{}) error {
	conn, err := z.getConnect()

	if err != nil {
		return errors.New("Can not connect db!")
	}
	defer conn.Close()

	return conn.Send(commandName, args...)
}

func (z *RedisDB) Flush() {
	if z.isConnect {
		conn := z.pool.Get()
		defer conn.Close()
		conn.Flush()
	}
}

func (z *RedisDB) Close() {
	if z.isConnect {
		z.pool.Close()
	}
}
