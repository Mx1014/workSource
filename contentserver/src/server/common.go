package server

import (
	"bufio"
	"fmt"
	"os"
	"path"
	"strconv"
	"strings"

	"gopkg.in/gographics/imagick.v2/imagick"
)

const (
	PROJECT_VERSION = "0.0.1"
	MAX_LINE        = 1024
	CACHE_KEY_SIZE  = 128
	RETRY_TIME_WAIT = 1000
	CACHE_MAX_SIZE  = 10485760
	PATH_MAX_SIZE   = 512
)

type ContentRequest struct {
	Md5     string
	ObjType string
}

type ImageRequest struct {
	Md5        string
	ImageType  string
	Width      int
	Height     int
	Proportion int
	Gary       int
	X          int
	Y          int
	Rotate     int
	Format     string
	Quality    int
	Save       int
}

type ImageInfo struct {
	Size    int    `json:"size"`
	Width   int    `json:"width"`
	Height  int    `json:"height"`
	Quality int    `json:"quality"`
	Format  string `json:"format"`
}

type ObjectInfo struct {
	Md5       string            `json:"md5"`
	ObjId     string            `json:objId`
	ObjType   string            `json:"objType"`
	Url       string            `json:"url"`
	Format    string            `json:"format"`
	ChunkSize int               `json:"chunkSize"`
	Filename  string            `json:"filename"`
	TotalSize int               `json:"totalSize"`
	Token     string            `json:"token"`
	Meta      map[string]string `json:"meta"`
	ParamsMap map[string]string `json:"paramsMap"`
}

type ClientResp struct {
	Uri string `json:"uri"`
	Url string `json:"url"`
}

type ObjectInfoResp struct {
	rv   int
	resp string
	uri  string
	url  string
}

type ServerContext struct {
	Config     AppConfig
	Logger     *ServerLogger
	Cache      *ServerCache
	Redisdb    *RedisDB
	LocalCache *LocalCache
	Bus        *MessageBus
}

func NewContext(configFile string) (*ServerContext, error) {
	imagick.Initialize()

	cfg, err := LoadConfig(configFile)
	if err != nil {
		return nil, err
	}

	//Fix image config
	cfg.System.ImageConfFile = strings.TrimSpace(cfg.System.ImageConfFile)
	if len(cfg.System.ImageConfFile) > 0 && cfg.System.ImageConfFile[0] != '/' {
		cfg.System.ImageConfFile = path.Join(path.Dir(configFile), cfg.System.ImageConfFile)
		//fmt.Printf("img.conf: " + cfg.System.ImageConfFile)
	}

	var log *ServerLogger
	logOutput := cfg.System.LogOutput
	if logOutput == "file" {
		log, err = NewFileLogger("contentserver", 0, cfg.System.LogName)
	} else if logOutput == "console" {
		log, err = NewLogger("contentserver", 0)
	} else {
		return nil, fmt.Errorf("init logger failed")
	}

	if err != nil {
		return nil, err
	}

	cache := NewCache(cfg.Storage.BeansdbHost, cfg.Storage.BeansdbPort)
	localCache := NewLocalCache(cfg.Storage.DiskvPath)
	var redisdb *RedisDB
	redisdb = nil
	if cfg.Storage.Sdb == 1 && len(cfg.Storage.RedisdbHost) > 0 {
		redisdb, _ = NewRedisDB(cfg.Storage.RedisdbHost, cfg.Storage.RedisdbPort)
	}

	return &ServerContext{Config: cfg, Logger: log, Cache: cache, Redisdb: redisdb, LocalCache: localCache}, nil
}

func parseQuery(url string) map[string]string {
	ret := make(map[string]string)

	for _, s := range strings.Split(url, "&") {
		ss := strings.Split(s, "=")
		key := strings.TrimSpace(ss[0])
		if len(ss) == 2 && len(key) > 0 {
			ret[key] = strings.TrimSpace(ss[1])
		}
	}
	return ret
}
func confGetInt(m map[string]string, x string, d int) int {
	if x, ok := m[x]; ok {
		if xx, err3 := strconv.ParseInt(x, 10, 64); err3 == nil {
			return int(xx)
		}
	}
	return d
}

func confGetStr(m map[string]string, x string, d string) string {
	if x, ok := m[x]; ok {
		return x
	}
	return d
}

func (s *ServerContext) LoadImageConfig() {
	confFile, err := os.Open(s.Config.System.ImageConfFile)
	if err != nil {
		s.Logger.Warning("Cannot read conf:%s\n", s.Config.System.ImageConfFile)
		return
	}
	defer confFile.Close()

	scanner := bufio.NewScanner(confFile)
	for scanner.Scan() {
		txt := strings.TrimSpace(scanner.Text())
		i := strings.Index(txt, "=")
		if i > 0 {
			name := strings.TrimSpace(txt[:i])
			params := strings.TrimSpace(txt[i+1:])
			m := parseQuery(params)
			imgCfg := &ImageConfig{}
			imgCfg.X = confGetInt(m, "x", -1)
			imgCfg.Y = confGetInt(m, "y", -1)
			imgCfg.Height = confGetInt(m, "h", 150)
			imgCfg.Width = confGetInt(m, "w", 150)
			imgCfg.Format = confGetStr(m, "f", "jpeg")
			imgCfg.Proportion = confGetInt(m, "p", 0)
			imgCfg.Rotate = confGetInt(m, "r", 0)
			imgCfg.Name = name
			//fmt.Println(imgCfg)
			s.Config.AddDynamicConfig(imgCfg)
		}
	}
}

func (s *ServerContext) Release() {
	s.Logger.Close()
	imagick.Terminate()
}
