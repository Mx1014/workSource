package server

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"path/filepath"
	"strconv"
	"strings"

	"github.com/gorilla/mux"
)

type ServerHttpd struct {
	context      *ServerContext
	imageStorage ImageStorage
	audioStorage AudioStorage
	fileStorage  FileStorage
	auth         Auth
	contentTypes map[string]string
}

type appHandler struct {
	*ServerHttpd
	h func(*ServerHttpd, http.ResponseWriter, *http.Request) (int, error)
}

func (ah appHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	status, err := ah.h(ah.ServerHttpd, w, r)
	if err != nil {
		ah.ServerHttpd.context.Logger.Debug("HTTP %d: %q\n", status, err)
		switch status {
		case http.StatusNotFound:
			http.NotFound(w, r)
			// And if we wanted a friendlier error page, we can
			// now leverage our context instance - e.g.
			// err := ah.renderTemplate(w, "http_404.tmpl", nil)
		case http.StatusInternalServerError:
			http.Error(w, http.StatusText(status), status)
		default:
			http.Error(w, http.StatusText(status), status)
		}
	}
}

func getContentTypes() map[string]string {
	types := make(map[string]string)
	types["jpg"] = "image/jpeg"
	types["jpeg"] = "image/jpeg"
	types["png"] = "image/png"
	types["gif"] = "image/gif"
	types["webp"] = "image/webp"

	return types
}

func serveHome(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	//var homeTempl = template.Must(template.ParseFiles("statics/test1.html"))
	//w.Header().Set("Content-Type", "text/html; charset=utf-8")
	//homeTempl.Execute(w, r.Host)

	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Headers", "*")

	io.WriteString(w, "<html>contentstorage home<br/>")
	io.WriteString(w, "<a href='/static/test1.html' target='_blank'>core emulator</a><br/>")
	io.WriteString(w, "<a href='/static/uploadtest.html' target='_blank'>image upload test</a><br/>")
	io.WriteString(w, "<a href='/static/audiotest.html' target='_blank'>audio upload test</a><br/>")
	io.WriteString(w, "</html>")

	return 200, nil
}

func serveImageUpload(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	if r.Method == "OPTIONS" {
		//fmt.Printf("options")
		w.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, X_Requested_With")
		return 200, nil
	}
	w.Header().Set("Access-Control-Allow-Headers", "*")

	obj := &ObjectInfo{ObjType: "image", Format: "jpeg", Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Creating(s, r, obj)
	if err != nil {
		return errcode, err
	}

	if err := r.ParseMultipartForm(CACHE_MAX_SIZE); err != nil {
		s.context.Logger.Error(err.Error())
		return http.StatusForbidden, fmt.Errorf("parse multi part error")
	}

	file, header, err := r.FormFile("upload_file")
	if err != nil {
		return 500, err
	}
	defer file.Close()

	data, err := ioutil.ReadAll(file)
	if err != nil {
		return 500, err
	}

	totalSize := len(data)
	s.context.Logger.Info("serve image data:%d", len(data))

	info, err := s.imageStorage.InfoImageFromData(data)
	if err != nil {
		return 403, err
	}
	obj.Meta["width"] = fmt.Sprintf("%d", info.Width)
	obj.Meta["height"] = fmt.Sprintf("%d", info.Height)

	md5, err := s.imageStorage.SaveImage(gen_md5_str(data), data)
	if err != nil {
		return 500, err
	}

	//Now the field of Md5 is a key from image storage
	obj.Md5 = md5
	obj.Filename = header.Filename
	obj.TotalSize = totalSize
	rsp, errcode, err = s.auth.Created(s, obj)
	if err != nil {
		return errcode, err
	}

	respStr := genClientResp(0, "Uploaded", &ClientResp{rsp.ObjectId, rsp.Url})
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)
	return 200, nil
}

func str2Int(str string, def int) int {
	str = strings.Trim(str, " ")
	if len(str) > 0 {
		i, err := strconv.Atoi(str)
		if err != nil {
			return def
		} else {
			return i
		}
	} else {
		return def
	}
}

func formIntValue(r *http.Request, key string, def int) int {
	return str2Int(r.FormValue(key), def)
}

func serveImageConfig(s *ServerHttpd, writer http.ResponseWriter, r *http.Request) (int, error) {
	name := "image_300_200_webp"
	imgCfg := &ImageConfig{
		Name:   name,
		X:      -1,
		Y:      -1,
		Format: "webp",
		Width:  300,
		Height: 200,
	}

	s.context.Config.AddDynamicConfig(imgCfg)
	io.WriteString(writer, fmt.Sprintf("ok, %s added %v", name, imgCfg))
	//io.WriteString(writer, fmt.Sprintf("test, %s added %v", name, s.context.Config.GetImageConfig(name)))

	return 200, nil
}

func serveImageConfigGet(s *ServerHttpd, writer http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	id := params["md5"]
	confName := params["config"]
	if len(id) <= 0 || len(confName) <= 0 {
		return 404, fmt.Errorf("Image not found!")
	}

	req := s.context.Config.GetImageConfig(confName)
	if req == nil {
		fmt.Printf("not found for %s\n", confName)
		return 404, fmt.Errorf("not found")
	}

	obj := &ObjectInfo{ObjType: "image", ObjId: id, Format: req.Format, Meta: make(map[string]string)}
	rsp, errcode, err := s.auth.Lookup(s, r, obj)
	if err != nil {
		return errcode, err
	}

	md5 := rsp.Md5
	request := &ImageRequest{
		Md5:        md5,
		ImageType:  "",
		Width:      req.Width,
		Height:     req.Height,
		Proportion: req.Proportion,
		Gary:       req.Gary,
		X:          req.X,
		Y:          req.Y,
		Rotate:     req.Rotate,
		Format:     req.Format,
		Quality:    req.Quality,
		Save:       req.Save,
	}

	return processImageRequest(s, request, writer, r)
}

func serveImageGet(s *ServerHttpd, writer http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	id := params["md5"]
	if len(id) <= 0 {
		return 404, fmt.Errorf("Image not found!")
	}

	var request *ImageRequest
	confName := r.FormValue("config")
	if len(confName) > 0 {
		req := s.context.Config.GetImageConfig(confName)
		if req == nil {
			fmt.Printf("not found for %s\n", confName)
			return 404, fmt.Errorf("not found")
		}

		request = &ImageRequest{
			Md5:        "",
			ImageType:  "",
			Width:      req.Width,
			Height:     req.Height,
			Proportion: req.Proportion,
			Gary:       req.Gary,
			X:          req.X,
			Y:          req.Y,
			Rotate:     req.Rotate,
			Format:     req.Format,
			Quality:    req.Quality,
			Save:       req.Save,
		}
	} else {
		var f string
		w := formIntValue(r, "w", 0)
		h := formIntValue(r, "h", 0)
		g := formIntValue(r, "g", 0)
		x := formIntValue(r, "x", -1)
		y := formIntValue(r, "y", -1)
		rotate := formIntValue(r, "r", 0)
		q := formIntValue(r, "q", 0)
		p := formIntValue(r, "p", 0)
		format := r.FormValue("f")

		if w < 0 {
			w = 0
		}
		if h < 0 {
			h = 0
		}
		if g != 1 {
			g = 0
		}
		if x < 0 {
			x = -1
		}
		if y < 0 {
			y = -1
		}
		if q <= 0 {
			q = s.context.Config.System.Quality
		} else if q > 100 {
			q = 100
		}
		if len(format) == 0 {
			f = s.context.Config.System.Format
		} else {
			format = strings.ToLower(format)
			formats := strings.Split(s.context.Config.Storage.AllowedTypes, ",")
			isExist := false
			for _, v := range formats {
				if format == v {
					isExist = true
				}
			}
			if !isExist {
				f = s.context.Config.System.Format
			} else {
				f = format
			}
		}

		request = &ImageRequest{
			Md5:        "",
			ImageType:  "",
			Width:      w,
			Height:     h,
			Proportion: p,
			Gary:       g,
			X:          x,
			Y:          y,
			Rotate:     rotate,
			Format:     f,
			Quality:    q,
			Save:       1,
		}
	}

	obj := &ObjectInfo{ObjType: "image", ObjId: id, Format: request.Format, Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Lookup(s, r, obj)
	if err != nil {
		return errcode, err
	}
	request.Md5 = rsp.Md5

	return processImageRequest(s, request, writer, r)
}

func parseRange(r *http.Request, dataLen int) (int, int, bool) {
	//http://stackoverflow.com/questions/3303029/http-range-header
	strRange := r.Header.Get("Range")
	if len(strRange) > 0 && strings.HasPrefix(strings.ToLower(strRange), "bytes=") {
		strValues := strings.Split(strRange, "=")
		if len(strValues) == 2 {
			strValues = strings.Split(strValues[1], "-")
			if len(strValues) == 2 {
				start64, err := strconv.ParseInt(strValues[0], 0, 32)
				if err != nil {
					goto RANGE_END
				}
				start := int(start64)
				end := 0
				if len(strValues[1]) == 0 {
					end = dataLen
				} else {
					end64, err := strconv.ParseInt(strValues[1], 0, 32)
					if err != nil {
						goto RANGE_END
					}
					end = int(end64)
				}
				if start >= 0 && end > 0 && end > start && start < dataLen {
					if end >= dataLen {
						end = dataLen
					}
					return start, end, true
				}
			}
		}
	}

RANGE_END:
	return 0, 0, false
}

func processImageRequest(s *ServerHttpd, request *ImageRequest, writer http.ResponseWriter, r *http.Request) (int, error) {
	s.context.Logger.Info("request is %v\n", request)
	data, err := s.imageStorage.GetImage(request)
	if err != nil {
		return 500, err
	} else if data == nil {
		return 404, fmt.Errorf("Resource not found")
	}

	if s.context.Config.System.Etag == 1 {
		newMd5 := gen_md5_str(data)

		ifNoneMatch := r.Header.Get("If-None-Match")
		if len(ifNoneMatch) == 0 {
			writer.Header().Set("Etag", newMd5)
		} else {
			if ifNoneMatch == newMd5 {
				s.context.Logger.Debug("Etag Matched Return 304 EVHTP_RES_NOTMOD.")
				return http.StatusNotModified, fmt.Errorf("Not Modified")
			} else {
				writer.Header().Set("Etag", newMd5)
			}
		}
	}

	headers := s.context.Config.System.Headers
	if len(headers) > 0 {
		arr := strings.Split(headers, ",")
		for i := 0; i < len(arr); i++ {
			header := arr[i]
			kvs := strings.Split(header, ":")
			writer.Header().Set(kvs[0], kvs[1])
		}
	}

	imageFormat := strings.ToLower(request.Format)
	if contentType, ok := s.contentTypes[imageFormat]; ok {
		writer.Header().Set("Content-Type", contentType)
		writer.Header().Set("Accept-Ranges", "bytes")
		if writer.Header().Get("Content-Encoding") == "" {
			writer.Header().Set("Content-Length", strconv.Itoa(len(data)))
		}

		//Resume download support
		start, end, isRangeOk := parseRange(r, len(data)-1)
		if isRangeOk {
			writer.Header().Set("Content-Range", fmt.Sprintf("bytes %d-%d/%d", start, end, len(data)))
			io.Copy(writer, bytes.NewReader(data[start:end+1]))
			if (end + 1) >= len(data) {
				return 200, nil
			}
			return 206, nil
		} else {
			io.Copy(writer, bytes.NewReader(data))
			return 200, nil
		}
	} else {
		return http.StatusForbidden, fmt.Errorf("can not found content type!!!")
	}
}

func serveAudioUpload(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	support_formats := map[string]bool{
		"m4a": true,
		"mp3": true,
	}
	format := params["format"]
	if !support_formats[format] {
		return 400, fmt.Errorf("format not found")
	}

	obj := &ObjectInfo{ObjType: "audio", Format: format, Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Creating(s, r, obj)
	if err != nil {
		return errcode, err
	}

	if err := r.ParseMultipartForm(CACHE_MAX_SIZE); err != nil {
		s.context.Logger.Error(err.Error())
		return http.StatusForbidden, fmt.Errorf("parse multi part error")
	}

	file, _, err := r.FormFile("upload_file")
	if err != nil {
		return 500, err
	}
	defer file.Close()

	data, err := ioutil.ReadAll(file)
	if err != nil {
		return 500, err
	}

	totalSize := len(data)
	data_md5 := gen_md5_str(data)
	md5, err := s.audioStorage.SaveAudio(data_md5, data, format)
	if err != nil {
		return 500, err
	}

	duration, err := s.context.LocalCache.getAudioDuration(data_md5, data)
	if err == nil {
		obj.Meta["duration"] = duration
	}

	obj.Md5 = md5
	obj.TotalSize = totalSize
	rsp, errcode, err = s.auth.Created(s, obj)
	if err != nil {
		return errcode, err
	}

	respStr := genClientResp(0, "Uploaded", &ClientResp{rsp.ObjectId, rsp.Url})
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)
	return 200, nil
}

func serveAudioGet(s *ServerHttpd, writer http.ResponseWriter, r *http.Request) (int, error) {
	content_formats := map[string]string{
		"m4a": "audio/mp4a-latm",
		"mp3": "audio/mpeg",
	}

	params := mux.Vars(r)
	id := params["md5"]
	f := r.FormValue("f")
	if len(id) <= 0 || (len(f) > 0 && content_formats[f] == "") {
		return 404, fmt.Errorf("Audio not found!")
	}

	obj := &ObjectInfo{ObjType: "audio", ObjId: id, Format: f, Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Lookup(s, r, obj)
	if err != nil {
		return errcode, err
	}
	md5 := rsp.Md5

	data, err := s.audioStorage.GetAudio(md5, f)
	if err != nil {
		s.context.Logger.Debug("got audio error, %v\n", err)
		return 400, err
	}

	headers := s.context.Config.System.Headers
	if len(headers) > 0 {
		arr := strings.Split(headers, ",")
		for i := 0; i < len(arr); i++ {
			header := arr[i]
			kvs := strings.Split(header, ":")
			writer.Header().Set(kvs[0], kvs[1])
		}
	}

	writer.Header().Set("Content-Type", content_formats[f])
	writer.Header().Set("Accept-Ranges", "bytes")
	if writer.Header().Get("Content-Encoding") == "" {
		writer.Header().Set("Content-Length", strconv.Itoa(len(data)))
	}

	//Resume download support
	start, end, isRangeOk := parseRange(r, len(data)-1)
	if isRangeOk {
		writer.Header().Set("Content-Range", fmt.Sprintf("bytes %d-%d/%d", start, end, len(data)))
		io.Copy(writer, bytes.NewReader(data[start:end+1]))
		if (end + 1) >= len(data) {
			return 200, nil
		}
		return 206, nil
	} else {
		io.Copy(writer, bytes.NewReader(data))
		return 200, nil
	}
}

func saveObjectInfo(s *ServerHttpd, item *ObjectInfo, data []byte) (bool, string, string) {
	md5 := gen_md5_str(data)
	if md5 != item.Md5 {
		return false, fmt.Sprintf("md5 not equal:%s, %s", md5, item.Md5), ""
	}

	var key string
	var err error
	if item.ObjType == "image" {
		//fix width/height for image
		info, err := s.imageStorage.InfoImageFromData(data)
		if err != nil {
			return false, "", ""
		}
		item.Meta["width"] = fmt.Sprintf("%d", info.Width)
		item.Meta["height"] = fmt.Sprintf("%d", info.Height)
		key, err = s.imageStorage.SaveImage(md5, data)
		if err != nil {
			return false, "", ""
		}
	} else if item.ObjType == "audio" {
		key, err = s.audioStorage.SaveAudio(md5, data, item.Format)
		if err != nil {
			return false, "", ""
		}
	}
	item.Md5 = key
	resp, _, err := s.auth.Created(s, item)
	if err != nil {
		return false, "", ""
	}

	return true, resp.ObjectId, resp.Url
}

func imageObjectCreate(s *ServerHttpd, w http.ResponseWriter, obj *ObjectInfo) (int, error) {
	var resp *ObjectInfoResp
	key := gen_image_md5(obj.Md5)
	if s.context.Cache.Exist(key) {
		obj.Md5 = key
		rsp, _, err := s.auth.Created(s, obj)
		if err != nil {
			resp = &ObjectInfoResp{-5, "SAVING_ERROR", "", ""}
		}
		resp = &ObjectInfoResp{1, "", rsp.ObjectId, rsp.Url}
	} else {
		if 0 > s.context.LocalCache.PutObjectInfo(obj) {
			return 400, fmt.Errorf("new objectInfo error", "", "")
		} else {
			resp = &ObjectInfoResp{0, "wait for chunking", "", ""}
		}
	}

	var respStr string
	if resp.rv < 0 {
		respStr = genClientResp(resp.rv, "Uploading", resp.resp)
	} else if resp.rv == 0 {
		respStr = genClientResp(resp.rv, "Chunking", resp.resp)
	} else {
		respStr = genClientResp(resp.rv, "Uploaded", &ClientResp{resp.uri, resp.url})
	}
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)

	return 200, nil
}

func audioObjectCreate(s *ServerHttpd, w http.ResponseWriter, obj *ObjectInfo) (int, error) {
	var resp *ObjectInfoResp
	key := gen_audio_md5(obj.Format, obj.Md5)
	fmt.Printf("audio md5 is:%v key is:%v\n", obj.Md5, key)
	if s.context.Cache.Exist(key) {
		obj.Md5 = key
		rsp, _, err := s.auth.Created(s, obj)
		fmt.Printf("rsp:%v\n", rsp)
		if err != nil {
			resp = &ObjectInfoResp{-5, "SAVING_ERROR", "", ""}
		}
		resp = &ObjectInfoResp{1, "", rsp.ObjectId, rsp.Url}
	} else {
		if 0 > s.context.LocalCache.PutObjectInfo(obj) {
			return 400, fmt.Errorf("new objectInfo error")
		} else {
			resp = &ObjectInfoResp{0, "wait for chunking", "", ""}
		}
	}

	respStr := genClientResp(resp.rv, "Uploaded", &ClientResp{resp.uri, resp.url})
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)

	return 200, nil
}

func serveObjectCreate(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	md5 := params["md5"]

	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		return 400, err
	}

	var obj ObjectInfo
	err = json.Unmarshal(body, &obj)
	if err != nil {
		return 400, err
	}
	obj.Md5 = md5

	_, errcode, err := s.auth.Creating(s, r, &obj)
	if err != nil {
		return errcode, err
	}

	//s.context.Logger.Info("Got newObject: %v\n", obj)
	if obj.ObjType == "image" {
		return imageObjectCreate(s, w, &obj)
	} else if obj.ObjType == "audio" {
		return audioObjectCreate(s, w, &obj)
	} else {
		return 400, fmt.Errorf("object type not found")
	}
}

func serveObjectDelete(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	id := params["md5"]
	obj := &ObjectInfo{ObjId: id, Meta: make(map[string]string)}
	_, errcode, err := s.auth.Deleting(s, r, obj)
	if err != nil {
		return errcode, err
	}

	_, errcode, err = s.auth.Deleted(s, obj)
	if err != nil {
		return errcode, err
	}

	fmt.Fprint(w, fmt.Sprintf("deleted id:%s", id))
	return 200, nil
}

func serveObjectThumbnail(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	io.WriteString(w, "got resp error")
	return 200, nil
}

func serveObjectChunk(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Headers", "*")
	if r.Method != "POST" {
		return 200, nil
	}

	params := mux.Vars(r)
	md5 := params["md5"]
	index, err := strconv.Atoi(params["index"])

	token := r.FormValue("token")
	if len(token) <= 0 || err != nil {
		return 400, fmt.Errorf("params error")
	}

	if err := r.ParseMultipartForm(CACHE_MAX_SIZE); err != nil {
		s.context.Logger.Error(err.Error())
		return http.StatusForbidden, fmt.Errorf("parse multi part error")
	}

	file, _, err := r.FormFile("upload_file")
	if err != nil {
		s.context.Logger.Info("print form file error")
		return 500, err
	}
	defer file.Close()

	data, err := ioutil.ReadAll(file)
	if err != nil || len(data) == 0 {
		return 400, fmt.Errorf("read file from multipart stream error")
	}

	s.context.Logger.Info("md5:%s index:%d data:%d", md5, index, len(data))
	resp := s.context.LocalCache.PutChunking(md5, index, data)
	var respStr string
	if resp.rv < 0 {
		respStr = genClientResp(resp.rv, "Chunking", resp.resp)
	} else if resp.rv == 0 {
		respStr = genClientResp(0, "Chunking", resp.resp)
	} else {
		respStr = genClientResp(0, "Uploaded", &ClientResp{resp.uri, resp.url})
	}
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)
	return 200, nil
}

func serveFileUpload(s *ServerHttpd, w http.ResponseWriter, r *http.Request) (int, error) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	if r.Method == "OPTIONS" {
		//fmt.Printf("options")
		w.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, X_Requested_With")
		return 200, nil
	}
	w.Header().Set("Access-Control-Allow-Headers", "*")

	obj := &ObjectInfo{ObjType: "file", Format: "", Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Creating(s, r, obj)
	if err != nil {
		return errcode, err
	}

	if err := r.ParseMultipartForm(CACHE_MAX_SIZE); err != nil {
		s.context.Logger.Error(err.Error())
		return http.StatusForbidden, fmt.Errorf("parse multi part error")
	}

	file, header, err := r.FormFile("upload_file")
	if err != nil {
		return 500, err
	}
	defer file.Close()

	data, err := ioutil.ReadAll(file)
	if err != nil {
		return 500, err
	}

	totalSize := len(data)
	md5 := gen_md5_str(data)
	meta := make(map[string]string)
	meta["filename"] = header.Filename
	meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	obj.Filename = header.Filename
	obj.Format = filepath.Ext(header.Filename)
	obj.Md5, err = s.fileStorage.SaveFile(md5, data, len(data), len(data), meta)
	obj.TotalSize = totalSize
	if err != nil {
		return http.StatusForbidden, err
	}
	rsp, errcode, err = s.auth.Created(s, obj)
	if err != nil {
		return errcode, err
	}

	respStr := genClientResp(0, "Uploaded", &ClientResp{rsp.ObjectId, rsp.Url})
	w.Header().Set("Content-Type", "application/json")
	fmt.Fprint(w, respStr)
	return 200, nil
}

func serveFileGet(s *ServerHttpd, writer http.ResponseWriter, r *http.Request) (int, error) {
	params := mux.Vars(r)
	id := params["md5"]
	if len(id) <= 0 {
		return 404, errors.New("Image not found!")
	}

	obj := &ObjectInfo{ObjType: "file", ObjId: id, Meta: make(map[string]string)}
	obj.Meta["X-Forwarded-Scheme"] = r.Header.Get("X-Forwarded-Scheme")
	rsp, errcode, err := s.auth.Lookup(s, r, obj)
	if err != nil {
		return errcode, err
	}

	md5 := rsp.Md5
	datach, _, meta, err := s.fileStorage.GetFile(md5)
	if err != nil {
		s.context.Logger.Debug("got file error, %v\n", err)
		return 400, err
	}
	//TODO for range datas
	data := <-datach

	headers := s.context.Config.System.Headers
	if len(headers) > 0 {
		arr := strings.Split(headers, ",")
		for i := 0; i < len(arr); i++ {
			header := arr[i]
			kvs := strings.Split(header, ":")
			writer.Header().Set(kvs[0], kvs[1])
		}
	}

	s.context.Logger.Debug("got file meta %v\n", meta)

	if filename, ok := meta["filename"]; ok {
		writer.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=%s", filename))
	}
	writer.Header().Set("Content-Type", "application/octet-stream")

	writer.Header().Set("Accept-Ranges", "bytes")
	if writer.Header().Get("Content-Encoding") == "" {
		writer.Header().Set("Content-Length", strconv.Itoa(len(data)))
	}

	//Resume download support
	start, end, isRangeOk := parseRange(r, len(data)-1)
	if isRangeOk {
		writer.Header().Set("Content-Range", fmt.Sprintf("bytes %d-%d/%d", start, end, len(data)))
		io.Copy(writer, bytes.NewReader(data[start:end+1]))
		if (end + 1) >= len(data) {
			return 200, nil
		}
		return 206, nil
	} else {
		io.Copy(writer, bytes.NewReader(data))
		return 200, nil
	}
}

var httpdGlobal *ServerHttpd

func GetHttpdGlobal() *ServerHttpd {
	return httpdGlobal
}

func StartServer(c *ServerContext) {
	//bus starting
	go bus.Run()

	addr := fmt.Sprintf("%s:%d", c.Config.System.Host, c.Config.System.Port)
	router := mux.NewRouter()
	var storage *SdbStorage
	if c.Config.Storage.Sdb == 0 {
		storage = NewBeansdbStorage(c)
	} else {
		storage = NewRedisdbStorage(c)
	}
	httpd := &ServerHttpd{
		context:      c,
		imageStorage: storage,
		audioStorage: storage,
		fileStorage:  storage,
		contentTypes: getContentTypes(),
		auth:         NewAuth(c.Config.System.Auth)}
	httpdGlobal = httpd

	go c.LocalCache.run(httpd)

	router.Handle("/", appHandler{httpd, serveHome}).Methods("GET")
	router.Handle("/config/image", appHandler{httpd, serveImageConfig})
	router.Handle("/upload/image", appHandler{httpd, serveImageUpload})
	router.Handle("/upload/audio/{format}", appHandler{httpd, serveAudioUpload})
	router.Handle("/image/{md5}", appHandler{httpd, serveImageGet}).Methods("GET")
	router.Handle("/audio/{md5}", appHandler{httpd, serveAudioGet}).Methods("GET")
	router.Handle("/image/{md5}/{config}", appHandler{httpd, serveImageConfigGet}).Methods("GET")
	router.Handle("/upload/file", appHandler{httpd, serveFileUpload})
	router.Handle("/file/{md5}", appHandler{httpd, serveFileGet}).Methods("GET")

	router.Handle("/object/create/{md5}", appHandler{httpd, serveObjectCreate})
	//router.Handle("/object/thunbnail/{md5}", appHandler{httpd, serveObjectThumbnail})
	router.Handle("/object/chunk/{md5}/{index}", appHandler{httpd, serveObjectChunk})
	router.Handle("/object/delete/{md5}", appHandler{httpd, serveObjectDelete})

	router.Handle("/ws", appHandler{httpd, serveWs})
	router.PathPrefix("/static/").Handler(http.StripPrefix("/static/", http.FileServer(http.Dir("./statics/"))))
	http.Handle("/", router)

	//Now load image config
	c.LoadImageConfig()

	c.Logger.Info("server version: 4.1.2\n start run :  %s", addr)
	http.ListenAndServe(addr, router)
}
