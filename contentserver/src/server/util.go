package server

import (
	"crypto/md5"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"math"
	"regexp"
	"strconv"
	"strings"
)

//web-safe-base64
//https://github.com/mochi/mochiweb/issues/37

func gen_md5_str(data []byte) string {
	m := md5.New()
	m.Write(data)
	return hex.EncodeToString(m.Sum(nil))
}

func encode_websafe64(s string) string {
	r, _ := regexp.Compile("=+$")
	s1 := base64.StdEncoding.EncodeToString([]byte(s))
	s2 := r.ReplaceAllString(s1, "")
	s3 := strings.Replace(s2, "/", "_", -1)
	s4 := strings.Replace(s3, "+", "-", -1)
	return s4
}

func decode_websafe64(s string) ([]byte, error) {
	s1 := strings.Replace(s, "-", "+", -1)
	s2 := strings.Replace(s1, "_", "/", -1)
	padding := len(s1) % 4
	switch padding {
	case 2:
		s2 += "=="
	case 3:
		s2 += "="
	}
	rlt, err := base64.StdEncoding.DecodeString(s2)
	return rlt, err
}

func gen_str_prefix(prefix string, md5 string) string {
	return encode_websafe64(fmt.Sprintf("%s:%s", prefix, md5))
}

func gen_audio_md5(format string, md5 string) string {
	return gen_str_prefix(fmt.Sprintf("2:%s", format), md5)
}

func gen_image_md5(md5 string) string {
	return gen_str_prefix("1", md5)
}

func gen_file_md5(md5 string) string {
	return gen_str_prefix("3", md5)
}

func key_decode(message string) (string, error) {
	//return url.QueryUnescape(message)
	rlt, err := decode_websafe64(message)
	if err != nil {
		return "", err
	}
	return string(rlt), nil
}

func is_md5(str string) bool {
	regular := `^([0-9a-zA-Z]){32}$`
	regx := regexp.MustCompile(regular)
	return regx.MatchString(str)
}

func gen_key(md5 string, args ...interface{}) string {
	s := []string{}
	s = append(s, md5)
	for _, argv := range args {
		switch v := argv.(type) {
		case string:
			s = append(s, v)
		case int:
			s = append(s, strconv.Itoa(v))
		}
	}
	return strings.Join(s, ":")
}

func round(val float64) float64 {
	if val > 0.0 {
		return math.Floor(val + 0.5)
	} else {
		return math.Ceil(val - 0.5)
	}
}
