//
// EvhLinkDTO.m
//
#import "EvhLinkDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLinkDTO
//

@implementation EvhLinkDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLinkDTO* obj = [EvhLinkDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerUid)
        [jsonObject setObject: self.ownerUid forKey: @"ownerUid"];
    if(self.sourceType)
        [jsonObject setObject: self.sourceType forKey: @"sourceType"];
    if(self.sourceId)
        [jsonObject setObject: self.sourceId forKey: @"sourceId"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.author)
        [jsonObject setObject: self.author forKey: @"author"];
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.contentAbstract)
        [jsonObject setObject: self.contentAbstract forKey: @"contentAbstract"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.deleterUid)
        [jsonObject setObject: self.deleterUid forKey: @"deleterUid"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerUid = [jsonObject objectForKey: @"ownerUid"];
        if(self.ownerUid && [self.ownerUid isEqual:[NSNull null]])
            self.ownerUid = nil;

        self.sourceType = [jsonObject objectForKey: @"sourceType"];
        if(self.sourceType && [self.sourceType isEqual:[NSNull null]])
            self.sourceType = nil;

        self.sourceId = [jsonObject objectForKey: @"sourceId"];
        if(self.sourceId && [self.sourceId isEqual:[NSNull null]])
            self.sourceId = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.author = [jsonObject objectForKey: @"author"];
        if(self.author && [self.author isEqual:[NSNull null]])
            self.author = nil;

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.contentAbstract = [jsonObject objectForKey: @"contentAbstract"];
        if(self.contentAbstract && [self.contentAbstract isEqual:[NSNull null]])
            self.contentAbstract = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.deleterUid = [jsonObject objectForKey: @"deleterUid"];
        if(self.deleterUid && [self.deleterUid isEqual:[NSNull null]])
            self.deleterUid = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
