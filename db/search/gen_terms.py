# -*- coding: UTF-8 -*-

txt = '''
			"subject":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
			"content":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "creatorNickName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contentcategory":{"type":"string", "index":"not_analyzed"},
            "actioncategory":{"type":"string", "index":"not_analyzed"},
            "identify": {"type":"string", "index":"not_analyzed"},
			"appId":{"type":"long"},
			"forumId":{"type":"long"},
			"categoryId":{"type":"long"},
			"creatorUid":{"type":"long"},
            "visibilityScopeId":{"type":"long"},
            "visibilityScope":{"type":"long"},
            "visibleRegionType":{"type":"long"},
            "visibleRegionId":{"type":"long"},
            "embeddedId":{"type":"long"},
			"senderName":{"type":"string","index":"no", "store":"yes"},
			"senderAvatar":{"type":"string", "index":"no", "store":"yes"},
			"forumName":{"type":"string", "index":"no", "store":"yes"},
			"displayName":{"type":"string", "index":"no", "store":"yes"},
			"behaviorTime":{"type":"date"}
'''

for t1 in txt.split('\n'):
    if t1:
        n = ""
        for t2 in t1.split('"'):
            t3 = t2.replace('\n', '').replace('\t','').replace(' ', '')
            if t3:
                n = t3
                break
        if n:
            print 'public static final String TERM_%s = "%s";' % (n.upper(), n)
