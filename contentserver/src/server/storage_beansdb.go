package server

func NewBeansdbStorage(c *ServerContext) *SdbStorage {
	beansdb := NewSdbStorage(c)
	beansdb.db = c.Cache

	return beansdb
}
