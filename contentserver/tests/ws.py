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
import websocket
import logging

requests.packages.urllib3.disable_warnings()

LOG_FILENAME = 'example.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)

logging.debug('This message should go to the log file')

ios = False
appId = 1
groupId = 1358
#currUser = 152736
currUser = 1025
targetUser = 3522

if len(sys.argv) > 1:
    currUser = 3522
    targetUser = 1025

#base_url = "http://10.1.1.92:8080"
#base_url = "http://10.1.1.98:8080"
#base_url = "http://testserver30.zuolin.com"
#base_url = "http://112.124.3.232"
#base_url = "http://ddd.tunnel.52miku.tk:18080"
#base_url = "https://testserver30.zuolin.com"
base_url = "https://core2.lab.everhomes.com/evh"
#base_url = "https://zuolin.com/evh"

users = [('13927485221','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1025, "a"), ('15889660710','', 3522, "b")\
            , ('13632878750','',10035,"c"), ('13823282798','',3429,"daiyunsss4"), ('13927485221', '', 152734, "dashi")\
            , ('12011112222','', 152736, 'datest'), ('15875300001','', 10020,'daw'), ('18675535761','', 152787,'daiyunsss4')\
            , ('18665353683', '', 1316, 'boge')\
            , ('18589029400', '', 72792, 'bbb') \
            , ('13824464512', 'ac13a74cce0474369de3af1b7dc2f4b9b35dfda0ae10c793f81a7a3c5e59a419', 10055, 'jiahua')]
#users = [('15889660710','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 10001, "a"), ('15889660710', '', 10001, "b")]
#users = [('15827099968','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 10015, "a"), ('13824464512', '', 10055, "b")]
#users = [('13138184726','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 10007, "a"), ('13138184726', '', 10007, "b")]

print  "argv", sys.argv, "curr", currUser, "target:", targetUser

class Client(object):
    def __init__(self):
        self.session = None
        self.token = None
        self.border = None
        self.ws = None
        self.cs = None
        self.requestId = 1
        self.deviceId = ''

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
    global currUser
    u = getUser(currUser)
    payload = {"namespaceId":0}
    payload["userIdentifier"] = u[0]
    payload["password"] = u[1]
    payload["deviceIdentifier"] = "frompython_%d_%s" % (u[2], u[3])
    #payload["deviceIdentifier"] = "frompython_%d_%s" % (0, u[3])
    if len(sys.argv) > 1:
        payload["deviceIdentifier"] = "frompythonb_%d_%s" % (u[2], u[3])

    if u[2] == 10004 and ios:
        payload["deviceIdentifier"] = "b135d649736eedd8dbf649a245a42856d400d13fbf96ecc0a2746fb670f09471"

    session = requests.Session()
    #r = session.post(base_url + "/user/logon", headers=headers, data=payload)
    r = session.post(base_url + "/user/logon", headers=headers, data=payload, verify=False)
    if r.status_code == 200:
        print r.text
        obj = json.loads(r.text)
        client.session = session
        if len(obj["response"]["accessPoints"]) > 0:
            client.border = obj["response"]["accessPoints"][0]
        client.token = obj["response"]["loginToken"]
        client.deviceId = payload["deviceIdentifier"]
        currUser = obj["response"]["uid"]
        print "updated curr User", currUser
        return True
    print r.text
    return False

def fetchMessages(client):
    payload = {"namespaceId":0, "appId": appId, "count": 200, "removeOld": 1}
    print "Goting message",
    r = client.session.post(base_url + "/user/fetchPastToRecentMessages", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        obj = json.loads(r.text)
        #print obj["response"]["nextPageAnchor"]
        if len(obj["response"]["messages"]) > 0:
            payload.update({"anchor":obj["response"]["messages"][-1]["storeSequence"]})
            r = client.session.post(base_url + "/user/fetchPastToRecentMessages", headers=headers, data=payload)
            print r.text
        return True
    print r
    return False

def fetchDeviceMessages(client):
    payload = {"deviceId":client.deviceId, "count": 10}
    r = client.session.post(base_url + "/pusher/recentMessages", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def syncGroup(client):
    r = client.session.post(base_url + "/admin/community/syncIndex", headers=headers, data={})
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def syncCommunity(client):
    r = client.session.post(base_url + "/admin/community/syncIndex", headers=headers, data={})
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def createFrame(name, payload):
    o = {"version":"0.01", "name": name, "requestId": client.requestId, "appId": 1, "payload":payload}
    client.requestId = client.requestId + 1
    return o

def on_open(ws):
    print "on_open"
    frame = createFrame("register", json.dumps({"loginToken": client.token}))
    print "send1", json.dumps(frame)
    ws.send(json.dumps(frame))

def on_message(ws, message):
    print "on_message", message
    obj = json.loads(message)
    if obj["name"] == "msg.stored":
        fetchMessages(client)
    if obj["name"] == "registedOk":
        frame = createFrame("AppIdStatusCommand", json.dumps({"name":"AppIdStatusCommand", "appIds": [1]}))
        print "send2", json.dumps(frame)
        ws.send(json.dumps(frame))

def on_error(ws, error):
    print "on_error", error
def on_close(ws):
    print "on_close"

def connectBorder(client):
    client.ws = websocket.WebSocket()
    client.ws.connect(client.border+"/client")

def sendMessage(client, body):
    payload = {'bodyType':'TEXT', 'body': body, 'appId': appId, 'deliveryOption': 7, 'senderUid': currUser, 'channels[0].channelType':"user", 'channels[0].channelToken': targetUser}
    payload.update({'channels[1].channelType':'user', 'channels[1].channelToken':currUser, 'meta[test]':'ccdd'})
    print "sending", payload
    r = client.session.post(base_url + "/user/sendMessage", headers=headers, data=payload, verify=False)
    if r.status_code == 200:
        print r.text
        return True
    print "error sendMessage", r
    return False

def sendAddressMessage(client, body):
    payload = {'bodyType':'TEXT', 'body': body, 'appId': appId, 'deliveryOption': 7, 'senderUid': currUser, 'channels[0].channelType':"address", 'channels[0].channelToken': 5018}
    payload.update({'channels[1].channelType':'user', 'channels[1].channelToken':currUser, 'meta[test]':'ccdd'})
    r = client.session.post(base_url + "/user/sendMessage", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def sendGroupMessage(client, body):
    payload = {'appId': appId, 'body':body, 'deliveryOption': 7, 'senderUid': currUser, 'channels[0].channelType':"group", 'channels[0].channelToken': groupId}
    r = client.session.post(base_url + "/user/sendMessage", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def searchCommunity(client, key):
    payload = {'cityId': 5636106, 'keyword': key}
    r = client.session.post(base_url + "/address/searchCommunities", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def searchGroup(client, key):
    payload = {'queryString': key}
    r = client.session.post(base_url + "/group/search", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def searchGlobalPosts(client, communityId, key):
    payload = {'communityId': communityId, 'queryString': key, 'searchFlag': 1}
    r = client.session.post(base_url + "/forum/search", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def searchSyncIndex(client):
    r = client.session.post(base_url + "/admin/group/syncGroupIndex", headers=headers)
    if r.status_code == 200:
        print r.text
        return True
    print r
    print r.text
    return False

def searchByKeyword(client, s):
    payload = {'queryString': s, 'pageSize': 5}
    r = client.session.post(base_url + "/group/search", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def testRecommand1(client, uid, sid):
    payload = {'userId': uid, 'sourceId': sid}
    r = client.session.post(base_url + "/recommend/testAddUser", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def testSync(client):
    r = client.session.post(base_url + "/forum/syncTest", headers=headers)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def testRecommand2(client):
    r = client.session.post(base_url + "/recommend/recommendUsers", headers=headers)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def on_push_open(ws):
    print "on_pusher_open", currUser
    u = getUser(currUser)
    deviceId = "frompython_%d_%s" % (u[2], u[3])
    if len(sys.argv) > 1:
        deviceId = "frompythonb_%d_%s" % (u[2], u[3])
    shakeMsg = {"deviceId": deviceId, "deviceType": "Android", "meta":{}}
    frame = createFrame("HANDSHAKE", json.dumps(shakeMsg))
    txt = json.dumps(frame)
    print "Sending ", txt
    ws.send(txt)

def on_push_message(ws, message):
    print "on_push_message", message
    resp = json.loads(message)

    if "name" in resp and resp["name"] == "NOTIFY":
        frame = createFrame("REQUEST", json.dumps({"count": "10"}))
        txt = json.dumps(frame)
        print "Sending ", txt
        ws.send(txt)
    elif "response" in resp and "messages" in resp["response"] and len(resp["response"]["messages"]) > 0:
        frame = createFrame("REQUEST", json.dumps({"count": "10", "anchor": resp["response"]["anchor"]}))
        txt = json.dumps(frame)
        print "Sending ", txt
        ws.send(txt)

def get_user_snashot(client):
    url = base_url + "/user/getUserSnapshotInfo?token=" + client.token + "&uid=10037&"
    print url
    headers.update({"If-None-Match":"-563460034"})
    r = client.session.get(url, headers=headers)
    print r.headers
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def ignore_commend(client, uid1, uid2):
    payload = {"recommendItems[0].sourceId": uid1, "recommendItems[0].suggestType": 0, "recommendItems[0].sourceType": 0, \
        "recommendItems[1].sourceId": uid2, "recommendItems[1].sourceType": 0, "recommendItems[1].suggestType": 0}

    r = client.session.post(base_url + "/recommend/ignoreRecommend", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    return False

def door_notify(client):
    payload = {"timestamp": 0, "nonce": 322, "contentType":"TEXT", "content": "hello", "phones[0]": "18675535761", "phones[1]":"13824464512"}
    r = client.session.post(base_url + "/openapi/notifyMessage", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    print r.text
    return False

def door_notify2(client):
    payload = {"timestamp": 0, "nonce": 322, "phones[0]": "18675535761", "phones[1]":"13824464512"}
    r = client.session.post(base_url + "/openapi/notifyDoorLock", headers=headers, data=payload)
    if r.status_code == 200:
        print r.text
        return True
    print r
    print r.text
    return False

if __name__ == "__main__":
    if not logon(client):
        print "logon error"
        sys.exit(-1)
    print "sending to", targetUser
    sendMessage(client, "hello xx: %d" % targetUser)
    #sendAddressMessage(client, "hello address: %d" % targetUser)
    #get_user_snashot(client)

    #fetchMessages(client)
    #fetchDeviceMessages(client)

    #syncCommunity(client)
    #testRecommand2(client)
    #testRecommand1(client, currUser, 666)
    #testSync(client)
    #ignore_commend(client, 10010, 152713)

    #syncGroup(client)
    #sendGroupMessage(client, "hello ok: %d" % groupId)
    #searchCommunity(client, u"通苑")
    #searchSyncIndex(client)
    #searchByKeyword(client, u"一会回家")
    #searchGlobalPosts(client, 8, u"物业")
    #searchGroup(client, u"足球")

    #door_notify2(client)

    print client.border
    if True:
        ws = websocket.WebSocketApp("wss://" + client.border + "/client",
                                  on_message = on_message,
                                  on_open = on_open,
                                  on_error = on_error,
                                  on_close = on_close)
    if False:
        ws = websocket.WebSocketApp("wss://" + client.border + "/pusher",
                                  on_message = on_push_message,
                                  on_open = on_push_open,
                                  on_error = on_error,
                                  on_close = on_close)
    #ws.run_forever()
    ws.run_forever(sslopt={"cert_reqs": websocket.ssl.CERT_NONE})
