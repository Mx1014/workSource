package server

import (
	"bytes"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"math"
	"os/exec"
	"strings"
	"time"

	"gopkg.in/gographics/imagick.v2/imagick"
)

type SdbStorage struct {
	*BaseStorage
	db Sdb
}

func NewSdbStorage(c *ServerContext) *SdbStorage {
	sdb := new(SdbStorage)
	sdb.BaseStorage = new(BaseStorage)
	sdb.BaseStorage.context = c

	return sdb
}

func (s *SdbStorage) SaveImage(key string, data []byte) (string, error) {
	var result error = nil
	md5 := gen_image_md5(key)
	s.context.Logger.Info("got md5:%s\n", md5)

	if s.db.Exist(md5) {
		//result = fmt.Errorf("File exists, Needn't Save.")
		return md5, nil
	}

	s.context.Logger.Info("md5 not found, begin to save\n")
	result = s.db.SetBytes(md5, data)
	if result != nil {
		return "", result
	}
	return md5, nil
}

func Round(val float64, roundOn float64, places int) (newVal float64) {
	var round float64
	pow := math.Pow(10, float64(places))
	digit := pow * val
	_, div := math.Modf(digit)
	if div >= roundOn {
		round = math.Ceil(digit)
	} else {
		round = math.Floor(digit)
	}
	newVal = round / pow
	return
}

func (s *SdbStorage) GetImage(request *ImageRequest) ([]byte, error) {
	var err error = nil
	var data []byte = nil
	var data2 []byte = nil
	var rspCacheKey string

	mw := imagick.NewMagickWand()
	defer mw.Destroy()

	md5 := request.Md5
	s.context.Logger.Info("start processing image request %v", md5)

	//TODO for ImageType
	if request.Proportion == 0 && request.Width == 0 && request.Height == 0 {
		s.context.Logger.Info("getting origin image")
		data, err = s.db.GetBytes(md5)
		if err != nil || len(data) == 0 {
			return nil, fmt.Errorf("Image %s is not existed.", md5)
		}
		return data, nil
	} else {
		//Get origin image
		data, err = s.db.GetBytes(md5)
		if err != nil {
			return nil, fmt.Errorf("Image %s is not existed.", md5)
		}

		err = mw.ReadImageBlob(data)
		if err != nil {
			s.context.Logger.Debug("Webimg Read Blob Failed!")
			return nil, err
		}

		width := int(mw.GetImageWidth())
		height := int(mw.GetImageHeight())
		//quality := int(mw.GetImageCompressionQuality())
		//format := mw.GetImageFormat()

		if request.Width == 0 && request.Height == 0 {
			request.Width = width
			request.Height = height
		} else if request.Width != 0 && request.Height == 0 {
			scale := float64(width) / float64(request.Width)
			request.Height = int(Round(float64(height)/scale, .5, 0))
		} else if request.Width == 0 && request.Height != 0 {
			scale := float64(height) / float64(request.Height)
			request.Width = int(Round(float64(width)/scale, .5, 0))
		}

		if request.Width > width {
			request.Width = width
		}
		if request.Height > height {
			request.Height = height
		}

		//Better for phone
		/* if request.X == -1 && request.Y == -1 && request.Width != width && request.Height != height {
			scale1 := float64(request.Width) / float64(width)
			scale2 := float64(request.Height) / float64(height)
			if scale1 > scale2 {
				newWidth := scale2 * float64(width)
				request.X = int((float64(width) - newWidth) / 2)
				request.Y = 0
			} else if scale2 > scale1 {
				newHeight := scale1 * float64(height)
				request.X = 0
				request.Y = int((float64(height) - newHeight) / 2)
			}
		} */

		rspCacheKey = gen_key(md5, request.Width, request.Height, request.Proportion, request.Gary, request.X, request.Y, request.Rotate, request.Quality, request.Format)

		data2, err = s.db.GetBytes(rspCacheKey)
		if err == nil && data2 != nil {
			//Founded
			s.context.Logger.Info("Found producted image")
			return data2, nil
		}

		//Product new image
		err = convert(mw, request)
		if err != nil {
			s.context.Logger.Info("Convert image failed")
			return nil, err
		}

		data = mw.GetImageBlob()
		s.context.Logger.Debug("get image blob length : %d", len(data))

		if data == nil || len(data) == 0 {
			return nil, fmt.Errorf("Webimg Get Blob Failed!")
		}

		if request.Save == 1 || s.context.Config.Storage.SaveNew == 1 {
			s.context.Logger.Debug("Image [%s] Saved to Storage.", rspCacheKey)
			if err = s.db.SetBytes(rspCacheKey, data); err != nil {
				s.context.Logger.Debug("New Image[%s] Save Failed!", rspCacheKey)
			}
		} else {
			s.context.Logger.Debug("Image [%s] Needn't to Storage.", rspCacheKey)
		}

		return data, nil
	} //End of get origin image
}

func (s *SdbStorage) InfoImage(id string) (*ImageInfo, error) {
	mw := imagick.NewMagickWand()
	defer mw.Destroy()

	return nil, nil
}

func (s *SdbStorage) InfoImageFromData(data []byte) (*ImageInfo, error) {
	mw := imagick.NewMagickWand()
	defer mw.Destroy()

	info := &ImageInfo{}
	err := mw.ReadImageBlob(data)
	if err != nil {
		s.context.Logger.Debug("Webimg Read Blob Failed!")
		return nil, err
	}

	info.Size = len(data)
	info.Width = int(mw.GetImageWidth())
	info.Height = int(mw.GetImageHeight())
	info.Quality = int(mw.GetImageCompressionQuality())
	info.Format = mw.GetImageFormat()

	return info, nil
}

func (s *SdbStorage) SaveAudio(key string, data []byte, format string) (string, error) {
	md5 := gen_audio_md5(format, key)
	if s.context.Cache.Exist(md5) {
		return md5, nil
	}

	//TODO retrieval duration from audio
	//file, err := ioutil.TempFile(os.TempDir(), "prefix")
	//defer os.Remove(file.Name())

	err := s.db.SetBytes(md5, data)
	if err != nil {
		s.context.Logger.Debug("save %s error\n", md5)
		return "", fmt.Errorf("save data error")
	}

	return md5, nil
}

func (s *SdbStorage) GetAudio(md5 string, format string) ([]byte, error) {
	origin, err := key_decode(md5)
	if err != nil {
		return nil, fmt.Errorf("md5 key error: %v", err)
	}
	//s.context.Logger.Info("origin key:%s\n", origin)
	ss := strings.Split(origin, ":")
	if len(ss) < 3 {
		return nil, fmt.Errorf("md5 key error")
	}

	var data []byte
	origin_format := ss[1]

	data, err = s.db.GetBytes(md5)
	if err != nil || len(data) == 0 {
		return nil, fmt.Errorf("data not found")
	}

	if len(format) == 0 || origin_format == format {
		return data, nil
	} else if origin_format == "m4a" && format == "mp3" {
		newkey := fmt.Sprintf("%s:mp3", md5)
		var data2 []byte
		data2, err = s.db.GetBytes(newkey)
		if err == nil && len(data2) > 0 {
			return data2, nil
		}
		data2, err = audio_m4a2mp3(data)
		if err != nil {
			return nil, fmt.Errorf("convert audio %s error: %v", md5, err)
		}

		err = s.db.SetBytes(newkey, data2)
		if err != nil {
			s.context.Logger.Debug("save audio error")
		}

		return data2, nil
	} else {
		return nil, fmt.Errorf("Format not support")
	}
}

func (s *SdbStorage) SaveFile(md5Key string, data []byte, frag int, total int, meta map[string]string) (string, error) {
	md5 := gen_file_md5(md5Key)
	s.context.Logger.Info("got md5:%s\n", md5)

	if frag == total && len(data) != frag {
		return "", errors.New("frag error")
	} else if len(data) > total {
		return "", errors.New("total size error")
	}

	if s.db.Exist(md5) {
		return md5, nil
	}

	if filename, ok := meta["filename"]; ok {
		s.context.Logger.Info("md5 not found: " + md5Key + " , begin to save, filename=" + filename)
	}
	if headerdata, err := fileMetaCreate(frag, total, meta); err != nil {
		return "", err
	} else {
		err = s.db.SetBytes(md5, append(headerdata, data...))
		if err != nil {
			return "", err
		}

		return md5, nil
	}
}

//TODO for range download
func (s *SdbStorage) GetFile(md5 string) (datach chan []byte, header *MetaHeader, meta map[string]string, err error) {
	data, err := s.db.GetBytes(md5)
	if err != nil || data == nil {
		return nil, nil, nil, fmt.Errorf("Image %s is not existed.", md5)
	}

	header, meta, err = fileMetaGet(data)
	if err != nil {
		return
	}

	datach = make(chan []byte)
	go func(header *MetaHeader, data []byte) {
		if header.fragSize == header.totalSize {
			datach <- data[header.metaLen+META_HEADER_LEN:]
		} else {
			datach <- data[header.metaLen+META_HEADER_LEN:]
			n := (header.totalSize / header.fragSize)
			for i := 1; i < n; i++ {
				key := fmt.Sprintf("%s:%d", md5, n)
				data, err = s.db.GetBytes(key)
				if err != nil || data == nil {
					break
				}

				datach <- data
			}
		}
		close(datach)
	}(header, data)

	return
}

func (s *SdbStorage) GetFileMeta(md5 string) (map[string]string, error) {
	data, err := s.db.GetBytes(md5)
	if err != nil || data == nil {
		return nil, fmt.Errorf("Image %s is not existed.", md5)
	}

	_, meta, err := fileMetaGet(data)
	if err != nil {
		return nil, err
	}

	return meta, err
}

func populateStdin(file []byte) func(io.WriteCloser) {
	return func(stdin io.WriteCloser) {
		defer stdin.Close()
		io.Copy(stdin, bytes.NewReader(file))
		fmt.Println("copied ok")
	}
}

func audio_m4a2mp3(data []byte) ([]byte, error) {
	binary, lookErr := exec.LookPath("ffmpeg")
	if lookErr != nil {
		return nil, fmt.Errorf("not found ffmpeg: %v\n", lookErr)
	}

	//ffmpeg := exec.Command(binary, "-i", "pipe:0", "-map", "0:a", "-b:a", "24k", "-f", "mp3", "pipe:1")
	ffmpeg := exec.Command(binary, "-i", "pipe:0", "-ab", "24k", "-f", "mp3", "pipe:1")
	stdin, _ := ffmpeg.StdinPipe()
	stdout, _ := ffmpeg.StdoutPipe()
	//stderr, _ := ffmpeg.StderrPipe()

	//echo := exec.Command("echo", os.Getenv("PATH"))
	//tmpout, _ := echo.StdoutPipe()
	//echo.Start()
	//go io.Copy(os.Stdout, tmpout)

	if err := ffmpeg.Start(); err != nil {
		return nil, fmt.Errorf("exec error, %v\n", err)
	}
	fmt.Println("started")

	go populateStdin(data)(stdin)
	//go io.Copy(os.Stderr, stderr)
	fmt.Println("input ok")

	data2, err := ioutil.ReadAll(stdout)
	if err != nil {
		return nil, fmt.Errorf("readall error, %v\n", err)
	}

	//TODO for more tests
	//http://stackoverflow.com/questions/11886531/terminating-a-process-started-with-os-exec-in-golang
	done := make(chan error, 1)
	go func() {
		done <- ffmpeg.Wait()
	}()
	select {
	case <-time.After(3 * time.Second):
		if err := ffmpeg.Process.Kill(); err != nil {
			log.Fatal("failed to kill: ", err)
		}
		<-done // allow goroutine to exit
		return nil, fmt.Errorf("process killed")
	case err := <-done:
		if err != nil {
			return nil, err
		}
	}

	return data2, err
}
