package server

import (
	"sync"

	"code.google.com/p/gcfg"
)

type ImageConfig struct {
	Name       string
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

type AppConfig struct {
	System struct {
		IsDaemon      int
		Host          string
		Port          int
		Headers       string
		Etag          int
		LogOutput     string
		LogLevel      int
		LogName       string
		Format        string
		Quality       int
		StaticPath    string
		Auth          int
		ImageConfFile string
	}

	Storage struct {
		Sdb          int
		SaveNew      int
		MaxSize      int
		AllowedTypes string
		BeansdbHost  string
		BeansdbPort  int
		RedisdbHost  string
		RedisdbPort  int
		DiskvPath    string
	}

	DynamicConfig struct {
		m         sync.RWMutex
		ImageCfgs map[string]*ImageRequest
		imageAdds chan *ImageConfig
	}
}

func (a *AppConfig) GetImageConfig(confName string) *ImageRequest {
	var req *ImageRequest
	defer func() {
		a.DynamicConfig.m.RUnlock()
	}()
	a.DynamicConfig.m.RLock()
	req = a.DynamicConfig.ImageCfgs[confName]
	return req
}

func (a *AppConfig) AddDynamicConfig(cfg *ImageConfig) {
	a.DynamicConfig.imageAdds <- cfg
}

func (a *AppConfig) RunDynamicConfig() {
	for {
		select {
		case cfg := <-a.DynamicConfig.imageAdds:
			name := cfg.Name
			req := &ImageRequest{
				Md5:        "",
				ImageType:  "",
				Width:      cfg.Width,
				Height:     cfg.Height,
				Proportion: cfg.Proportion,
				Gary:       cfg.Gary,
				X:          cfg.X,
				Y:          cfg.Y,
				Rotate:     cfg.Rotate,
				Format:     cfg.Format,
				Quality:    cfg.Quality,
				Save:       cfg.Save,
			}
			a.DynamicConfig.m.Lock()
			a.DynamicConfig.ImageCfgs[name] = req
			a.DynamicConfig.m.Unlock()
			//fmt.Printf("cfg %s added %v\n", name, a.DynamicConfig.ImageCfgs[name])
		}
	}
}

func LoadConfig(cfgFile string) (AppConfig, error) {
	var err error
	var cfg AppConfig

	err = gcfg.ReadFileInto(&cfg, cfgFile)
	if err != nil {
		return cfg, err
	}

	cfg.DynamicConfig.ImageCfgs = make(map[string]*ImageRequest)
	cfg.DynamicConfig.imageAdds = make(chan *ImageConfig, 10)

	go cfg.RunDynamicConfig()

	return cfg, nil
}
