package server

func NewRedisdbStorage(c *ServerContext) *SdbStorage {
	redisdb := NewSdbStorage(c)
	redisdb.db = c.Redisdb

	return redisdb
}
