package server

type Sdb interface {
	Retry()
	Exist(key string) bool
	/* Get(key string) (string, error)
	Set(key string, v string) error */
	GetBytes(key string) ([]byte, error)
	SetBytes(key string, v []byte) error
	Del(key string) error
}
