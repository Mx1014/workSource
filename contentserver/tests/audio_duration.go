package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os/exec"
	"path/filepath"
	"time"

	"github.com/peterbourgon/diskv"
)

const (
	transformBlockSize = 8
)

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

func audio_info(d *diskv.Diskv, key string, data []byte) ([]byte, error) {
	d.Write(key, data)
	dest := pathFor(d, key)
	fmt.Printf("transform: %v\n", dest)
	ffprobe := exec.Command("ffprobe", "-v", "quiet", "-print_format", "json", "-show_format", dest)
	fmt.Printf("%v\n", ffprobe)
	stdin, _ := ffprobe.StdinPipe()
	stdout, _ := ffprobe.StdoutPipe()
	if err := ffprobe.Start(); err != nil {
		return nil, fmt.Errorf("exec error, %v\n", err)
	}
	fmt.Println("started")

	stdin.Close()
	data2, _ := ioutil.ReadAll(stdout)

	done := make(chan error, 1)
	go func() {
		done <- ffprobe.Wait()
	}()
	select {
	case <-time.After(3 * time.Second):
		if err := ffprobe.Process.Kill(); err != nil {
			log.Fatal("failed to kill: ", err)
		}
		<-done // allow goroutine to exit
		return nil, fmt.Errorf("process killed")
	case err := <-done:
		if err != nil {
			return nil, err
		}
	}

	return data2, nil
}

func blockTransform(s string) []string {
	var (
		sliceSize = len(s) / transformBlockSize
		pathSlice = make([]string, sliceSize)
	)
	for i := 0; i < sliceSize; i++ {
		from, to := i*transformBlockSize, (i*transformBlockSize)+transformBlockSize
		pathSlice[i] = s[from:to]
	}
	return pathSlice
}

func pathFor(d *diskv.Diskv, key string) string {
	return filepath.Join(d.BasePath, filepath.Join(d.Transform(key)...), key)
}

func main() {
	d := diskv.New(diskv.Options{
		BasePath:     "/tmp/my-data-dir",
		Transform:    blockTransform,
		CacheSizeMax: 1024 * 1024,
	})

	filename := "/home/janson/workspace-go/src/git.lab.everhomes.com/eh-dev/contentserver/src/server/build/statics/382c7388.m4a"
	buf, _ := ioutil.ReadFile(filename)
	out, _ := audio_info(d, "testf", buf)

	var obj interface{}
	json.Unmarshal(out, &obj)
	fmt.Printf("%v\n", obj)
}
