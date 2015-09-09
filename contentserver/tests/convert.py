import codecs
import base64

tmp = "insert into eh_content_server_resources(id, owner_id, resource_id, resource_md5, resource_type, resource_size) values(%d, 1, '%s', '%s', %s, %s);"
idd = 2
with open("z.trans", "r") as f:
    for line in f:
        ss = line.split(":")
        s = ss[0]
        s = s.split("/")[1]
        s = s + '=='
        rlt = base64.urlsafe_b64decode(s)
        #print ss
        #print rlt
        sql = tmp % (idd, rlt, rlt.split("/")[1], ss[1], ss[2])
        print sql
        idd = idd + 1
