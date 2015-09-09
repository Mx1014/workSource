# -*- coding: UTF-8 -*-
import os
import io
from io import BytesIO
import urllib3
import StringIO
import requests
import json
import hashlib
from PIL import Image

api_url = 'http://127.0.0.1:5000'
def thumbnail(infile, max_width):
    try:
        img = Image.open(infile)
        s = img.size;
        if s[0] > max_width:
            ratio = max_width/s[0];
            size = (s[0]*ratio, s[1]*ratio)
            img.thumbnail(size, Image.ANTIALIAS)
        output = StringIO.StringIO()
        img.save(output, "JPEG")
        return output
    except IOError:
        print "cannot create thumbnail for '%s'" % infile
        return None

def upload(api_url, my_file, filename):
    r = None
    try:
        r = requests.post(api_url, files={"upload_file": my_file})
    except:
        print "error for ", filename

def create_object(api_url, md5):
    url = api_url + "/object/create/"+ md5 +"?token=xxxxx"
    postObj = {"objType":"image", "format":"jpeg", "chunkSize": 10*1024, "totalSize": 100*1024, "meta": {}}
    r = requests.post(url, data=json.dumps(postObj))
    print "resp is ", r.text

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

def uploadimage_with_chunk(api_url, filename):
    _, ext = os.path.splitext(filename)
    chunkSize = 100*1024

    with open(filename, 'rb') as f:
        old_file_position = f.tell()
        f.seek(0, os.SEEK_END)
        size = f.tell()
        f.seek(old_file_position, os.SEEK_SET)
        md5 = hashlib.md5(f.read()).hexdigest()

        f.seek(0, os.SEEK_SET)

        fileName, fileExtension = os.path.splitext(filename)
        objType = "image"
        if fileExtension == ".m4a" or fileExtension == ".mp3":
            objType = "audio"

        url = api_url + "/object/create/"+ md5 +"?token=eyJ1c2VySWQiOjExMDA0LCJsb2dpbklkIjoxLCJsb2dpbkluc3RhbmNlTnVtYmVyIjotNTE4MTkwNzA5fQ&a=bbb&d=hlelo"
        postObj = {"objType":objType, "format":ext[1:], "chunkSize": chunkSize, "totalSize": size, "filename": filename, "meta": {}}
        r = requests.post(url, data=json.dumps(postObj))
        print "resp is ", r.status_code, r.text

        count = size / chunkSize
        if count == 0:
            count = 1
        print "count is ", count, chunkSize, size, md5

        readSize = 0
        for i in range(count):
            url = api_url + "/object/chunk/"+ md5 + "/" + str(i) + "?token=xxxxx"

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
            r = requests.post(url, data=body, headers=headers)
            print "resp is ", r.status_code, r.text

        print readSize, size

files = ['/home/janson/download/z.jpg', '/home/janson/download/indexbg.png', '/home/janson/download/3.jpg', '/home/janson/download/h.jpg']
#create_object(api_url, "4d25480427c6c7fdca6c4b5a0316d841")
uploadimage_with_chunk(api_url, files[-1])

