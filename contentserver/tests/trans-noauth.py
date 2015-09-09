# -*- coding: UTF-8 -*-
import os
import io
from io import BytesIO
import urllib3
import StringIO
import requests
import json
import hashlib
import sys
import base64

#base_path = "/home/samba/share/res"
base_path = "/home/janson/projects/elastic/tmp/mirflickr"
base_url = "http://10.1.1.92:8080"

class Client(object):
    def __init__(self):
        self.session = None
        self.token = "aa"
        self.border = None
        self.ws = None
        self.cs = "127.0.0.1:5001"
        self.requestId = 1
        self.cnt = 0

headers = {'User-Agent': 'Mozilla/5.0'}
client = Client()

def getUser(uid):
    x = []
    for u in users:
        if u[2] == uid:
            x = [a for a in u]
    if x is not None and x[1] == '':
        x[1] = users[0][1]
    return x

def logon(client):
    session = requests.Session()
    client.session = session
    return True

def work_all_files(folder_path):
    for f in os.listdir(folder_path):
        path = os.path.join(folder_path, f)
        if os.path.isfile(path):
            if any(f.lower().endswith(ext) for ext in ['.jpg', '.png', 'gif']):
                yield "image", path
            elif any(f.lower().endswith(ext) for ext in ['.m4a', '.mp3']):
                yield "audio", path
        elif os.path.isdir(path):
            for t,myf in work_all_files(path):
                yield t, myf

def upload(client, t, path):
    my_file = open(path, 'rb')
    if t == "image":
        api_url = "http://" + client.cs + "/upload/" + t + "?token=" + client.token
    else:
        ext = path[path.rfind(".")+1:]
        api_url = "http://" + client.cs + "/upload/" + t + "/" + ext + "?token=" + client.token
    print api_url
    r = None
    try:
        r = client.session.post(api_url, files={"upload_file": my_file})

        if r.status_code == 200:
            print r.text
            obj = json.loads(r.text)
            if obj["errorCode"] == 0:
                return obj["response"]["uri"]

        print "resp error ", r.status_code, r.text
        return None
    except:
        print "upload error "
        return None
    finally:
        my_file.close()

def upload_auto(client, path):
    fileName, ext = os.path.splitext(path)
    if fileName.find("thumbnail") >= 0:
        return None

    #print path
    client.cnt = client.cnt + 1
    if client.cnt % 1000 == 0:
        print "Upload ", client.cnt
    objType = "image"
    if ext == ".m4a" or ext == ".mp3":
        objType = "audio"

    if objType == "image":
        api_url = "http://" + client.cs + "/upload/" + objType + "?token=" + client.token
    elif objType == "audio":
        ext = path[path.rfind(".")+1:]
        api_url = "http://" + client.cs + "/upload/" + objType + "/" + ext + "?token=" + client.token
    else:
        return None

    r = None
    with open(path, 'rb') as my_file:
        try:
            r = client.session.post(api_url, files={"upload_file": my_file})

            if r.status_code == 200:
                obj = json.loads(r.text)
                if obj["errorCode"] == 0:
                    return obj["response"]["uri"]

            print "resp error ", r.status_code, r.text
            return None
        except:
            print "upload error "
            return None

class CancelledError(Exception):
    def __init__(self, msg):
        self.msg = msg
        Exception.__init__(self, msg)

    def __str__(self):
        return self.msg

    __repr__ = __str__

class BufferReader(BytesIO):
    def __init__(self, buf=b'',
                 callback=None,
                 cb_args=(),
                 cb_kwargs={}):
        self._callback = callback
        self._cb_args = cb_args
        self._cb_kwargs = cb_kwargs
        self._progress = 0
        self._len = len(buf)
        BytesIO.__init__(self, buf)

    def __len__(self):
        return self._len

    def read(self, n=-1):
        chunk = BytesIO.read(self, n)
        self._progress += int(len(chunk))
        self._cb_kwargs.update({
            'size'    : self._len,
            'progress': self._progress
        })
        if self._callback:
            try:
                self._callback(*self._cb_args, **self._cb_kwargs)
            except: # catches exception from the callback
                raise CancelledError('The upload was cancelled.')
        return chunk

def progress(size=None, progress=None):
    pass
    #print("{0} / {1}".format(size, progress))

def upload_with_chunk(client, filename, chunkSize):
    _, ext = os.path.splitext(filename)
    print filename

    with open(filename, 'rb') as f:
        old_file_position = f.tell()
        f.seek(0, os.SEEK_END)
        size = f.tell()
        f.seek(old_file_position, os.SEEK_SET)
        md5 = hashlib.md5(f.read()).hexdigest()

        f.seek(0, os.SEEK_SET)

        #print "chunkSize", chunkSize, "totalSize", size

        fileName, fileExtension = os.path.splitext(filename)
        objType = "image"
        if fileExtension == ".m4a" or fileExtension == ".mp3":
            objType = "audio"

        url = "http://" + client.cs + "/object/create/"+ md5 + "?token=" + client.token
        #print url
        postObj = {"objType":objType, "format":ext[1:], "chunkSize": chunkSize, "totalSize": size, "filename": filename, "meta": {}}
        r = client.session.post(url, data=json.dumps(postObj))
        if r.status_code == 200:
            obj = json.loads(r.text)
            if obj["errorCode"] == 0 and "uri" in obj["response"] and len(obj["response"]["uri"]) > 0:
                return obj["response"]["uri"]
        else:
            return None

        count = size / chunkSize
        if count == 0:
            count = 1
        #print "count is ", count, chunkSize, size, md5

        readSize = 0
        for i in range(count):
            url = "http://" + client.cs + "/object/chunk/"+ md5 + "/" + str(i) + "?token=" + client.token

            if i == count-1:
                chunkBytes = f.read(size - readSize)
            else:
                chunkBytes = f.read(chunkSize)
            readSize += len(chunkBytes)

            (data, ctype) = urllib3.filepost.encode_multipart_formdata({"upload_file": ("upload_file.jpeg", chunkBytes)})
            headers = {
                "Content-Type": ctype
            }
            body = BufferReader(data, progress)
            r = client.session.post(url, data=body, headers=headers)
            if r.status_code == 200:
                obj = json.loads(r.text)
                if obj["errorCode"] == 0 and "uri" in obj["response"] and len(obj["response"]["uri"]) > 0:
                    return obj["response"]["uri"]
    print "upload ", filename, " error"
    return None

def tran_all_images(client, folder_path, out_path):
    tmap = {"image": 1, "audio": 2}
    with open(out_path, 'w') as out_file:
        for t, path in work_all_files(folder_path):
            fsize = os.path.getsize(path)
            #uri = upload_with_chunk(client, path, fsize)
            uri = upload_auto(client, path)
            if uri is not None:
                #uri = uri[uri.rfind("/")+1:]
                uri = base64.urlsafe_b64encode(uri).replace('=','')
                sout = "%s/%s:%s:%d:%s" % (t, uri, tmap[t], fsize, path[len(folder_path)+1:])
                out_file.write(sout + "\n")

if __name__ == "__main__":
    if not logon(client):
        print "logon error"
        sys.exit(-1)
    #p = "/home/janson/download/zz1.jpg"
    #print upload_with_chunk(client, p, os.path.getsize(p))
    #print upload_auto(client, p)
    tran_all_images(client, base_path, "z.trans")
