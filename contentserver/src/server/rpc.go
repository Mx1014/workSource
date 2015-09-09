package server

import (
	"encoding/json"
	"fmt"
	"strings"
)

const (
	rpcVersion     = ""
	rpcAppId       = 8888
	accessAuth     = "AUTH"
	accessUploaded = "UPLOADED"
)

type RpcAccessRequest struct {
	MessageType string            `json:"messageType"`
	Token       string            `json:"token"`
	Md5         string            `json:"md5"`
	ObjectType  string            `json:"objectType"`
	ObjectId    string            `json:"objectId"`
	Format      string            `json:"format"`
	Filename    string            `json:"filename"`
	TotalSize   int               `json:"totalSize"`
	Meta        map[string]string `json:"meta"`
	ParamsMap   map[string]string `json:"paramsMap"`
	Ext         string            `json:"ext"`
}

type RpcAccessResponse struct {
	MessageType string            `json:"messageType"`
	Token       string            `json:"token"`
	Md5         string            `json:"md5"`
	ObjectId    string            `json:"objectId"`
	Url         string            `json:"url"`
	Filename    string            `json:"filename"`
	TotalSize   int               `json:"totalSize"`
	Meta        map[string]string `json:"meta"`
	ParamsMap   map[string]string `json:"paramsMap"`
	ErrCode     int               `json:"errCode"`
	ErrMsg      string            `json:"errMsg"`
}

type RpcImageConfigProps struct {
	Width      int    `json:"width"`
	Height     int    `json:"height"`
	Proportion int    `json:"proportion"`
	Gary       int    `json:"gary"`
	X          int    `json:"x"`
	Y          int    `json:"y"`
	Rotate     int    `json:"rotate"`
	Format     string `json:"format"`
	Quality    int    `json:"quality"`
	Save       int    `json:"save"`
}

type RpcImageConfig struct {
	ConfigName  string              `json:"configName"`
	ConfigType  string              `json:"configType"`
	ConfigProps RpcImageConfigProps `json:"configProps"`
}

func RequestAccess(msgType string, obj *ObjectInfo) (*RpcAccessResponse, error) {
	tmpss := strings.Split(msgType, ".")
	accReq := &RpcAccessRequest{
		MessageType: strings.ToUpper(tmpss[0]),
		Token:       obj.Token,
		Md5:         obj.Md5,
		ObjectType:  obj.ObjType,
		ObjectId:    obj.ObjId,
		Filename:    obj.Filename,
		TotalSize:   obj.TotalSize,
		Format:      obj.Format,
		Meta:        obj.Meta,
		Ext:         "",
	}

	payload, _ := json.Marshal(accReq)
	wsMsg := NewWsMessage("request."+strings.ToLower(msgType), string(payload))

	//Do request
	resp, err := wsMsg.GetResp()
	if err != nil {
		return nil, err
	}
	accResp := &RpcAccessResponse{}
	err = json.Unmarshal([]byte(resp), &accResp)
	fmt.Printf("Decode resp %v\n", accResp)
	if err != nil {
		return nil, fmt.Errorf("decode resp error")
	}

	return accResp, nil
}

func ParseRequest(s *ServerHttpd, c *Connection, msg *Message) {
	if strings.HasPrefix(msg.Name, "contentstorage.request.config.image") {
		s.context.Logger.Info("got image config\n")
		config := &RpcImageConfig{}
		err := json.Unmarshal([]byte(msg.Payload), &config)
		if err != nil {
			s.context.Logger.Debug("unable to parse response %v\n", err)
			return
		}
		cfg := &config.ConfigProps
		imgCfg := &ImageConfig{
			Name:    config.ConfigName,
			X:       cfg.X,
			Y:       cfg.Y,
			Format:  cfg.Format,
			Width:   cfg.Width,
			Height:  cfg.Height,
			Gary:    cfg.Gary,
			Rotate:  cfg.Rotate,
			Quality: cfg.Quality,
			Save:    cfg.Save,
		}
		s.context.Config.AddDynamicConfig(imgCfg)

		resp := make(map[string]interface{})
		resp["configName"] = config.ConfigName
		resp["configType"] = "IMAGE"
		resp["errCode"] = 0
		resp["errMsg"] = ""
		payload, _ := json.Marshal(resp)
		respMsg := &Message{rpcVersion, "response.config.image" + "." + config.ConfigName, msg.RequestId, rpcAppId, ""}
		respMsg.Payload = string(payload)
		c.sendMsg <- respMsg
	}
}

func genClientResp(v int, name string, resp interface{}) string {
	respJson := make(map[string]interface{})
	respJson["version"] = ""
	respJson["errorScope"] = ""
	respJson["errorCode"] = 0
	respJson["errorDescription"] = ""
	respJson["name"] = name
	respJson["response"] = resp
	respStr, _ := json.Marshal(respJson)
	return string(respStr)
}
