package server

import (
	"fmt"
	"net/http"
	"strings"
)

type Auth interface {
	Lookup(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error)
	Creating(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error)
	Access(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error)
	Created(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error)
	Deleting(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error)
	Deleted(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error)
}

type AuthWs struct {
}

type AuthNone struct {
}

func NewAuth(isAuth int) Auth {
	if isAuth > 0 {
		return new(AuthWs)
	} else {
		return new(AuthNone)
	}
}

func authNone(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	if obj.Md5 == "" {
		obj.Md5 = obj.ObjId
	} else {
		obj.ObjId = obj.Md5
	}

	return &RpcAccessResponse{obj.ObjType, obj.Token, obj.Md5, obj.ObjId, obj.Url, obj.Filename, obj.TotalSize, obj.Meta, obj.ParamsMap, 0, ""}, 0, nil
}

func (*AuthNone) Access(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, nil, obj)
}

func (*AuthNone) Lookup(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, r, obj)
}

func (*AuthNone) Creating(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, r, obj)
}

func (*AuthNone) Created(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, nil, obj)
}

func (*AuthNone) Deleting(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, nil, obj)
}

func (*AuthNone) Deleted(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return authNone(s, nil, obj)
}

func HttpRequestAccess(msgType string, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	rsp, err := RequestAccess(msgType, obj)
	if err != nil {
		return nil, 400, err
	}
	if rsp.ErrCode != 0 {
		if strings.Index(rsp.ErrMsg, "INVALID LOGON") >= 0 {
			return nil, 401, fmt.Errorf(rsp.ErrMsg)
		} else {
			return nil, 403, fmt.Errorf(rsp.ErrMsg)
		}
	}
	if rsp == nil {
		return nil, 501, fmt.Errorf("core server error")
	}

	return rsp, 0, nil
}

func (*AuthWs) Access(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return HttpRequestAccess(accessAuth+".UPLOAD", obj)
}

func (*AuthWs) Lookup(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	token := r.FormValue("token")
	if len(token) <= 0 {
		return nil, 400, fmt.Errorf("token error")
	}
	obj.Token = token
	params := make(map[string]string)
	for k, v := range r.URL.Query() {
		if k == "token" {
			continue
		}
		params[k] = v[0]
	}
	obj.ParamsMap = params
	return HttpRequestAccess(accessAuth+".LOOKUP", obj)
}

func (*AuthWs) Creating(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	token := r.FormValue("token")
	if len(token) <= 0 {
		return nil, 400, fmt.Errorf("token error")
	}
	obj.Token = token
	params := make(map[string]string)
	for k, v := range r.URL.Query() {
		if k == "token" {
			continue
		}
		params[k] = v[0]
	}
	obj.ParamsMap = params
	return HttpRequestAccess(accessAuth+".UPLOAD", obj)
}

func (*AuthWs) Created(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return HttpRequestAccess(accessUploaded+".UPLOAD", obj)
}

func (*AuthWs) Deleting(s *ServerHttpd, r *http.Request, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	token := r.FormValue("token")
	if len(token) <= 0 {
		return nil, 400, fmt.Errorf("token error")
	}
	params := make(map[string]string)
	for k, v := range r.URL.Query() {
		if k == "token" {
			continue
		}
		params[k] = v[0]
	}
	obj.ParamsMap = params
	obj.Token = token
	return HttpRequestAccess(accessAuth+".DELETE", obj)
}

func (*AuthWs) Deleted(s *ServerHttpd, obj *ObjectInfo) (*RpcAccessResponse, int, error) {
	return HttpRequestAccess(accessAuth+"DELETE.DELETED", obj)
}
