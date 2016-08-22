package server

type ImageStorage interface {
	SaveImage(md5 string, data []byte) (string, error)
	GetImage(request *ImageRequest) ([]byte, error)
	InfoImage(id string) (*ImageInfo, error)
	InfoImageFromData(data []byte) (*ImageInfo, error)
}

type AudioStorage interface {
	SaveAudio(md5 string, data []byte, format string) (string, error)
	GetAudio(md5 string, format string) ([]byte, error)
}

type FileStorage interface {
	SaveFile(md5 string, data []byte, frag int, total int, meta map[string]string) (string, error)
	GetFile(md5 string) (chan []byte, *MetaHeader, map[string]string, error)
	GetFileMeta(md5 string) (map[string]string, error)
}

type BaseStorage struct {
	context *ServerContext
}
