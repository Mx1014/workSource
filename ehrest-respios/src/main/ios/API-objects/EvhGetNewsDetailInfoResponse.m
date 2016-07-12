//
// EvhGetNewsDetailInfoResponse.m
//
#import "EvhGetNewsDetailInfoResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsDetailInfoResponse
//

@implementation EvhGetNewsDetailInfoResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetNewsDetailInfoResponse* obj = [EvhGetNewsDetailInfoResponse new];
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
    if(self.theNewsToken)
        [jsonObject setObject: self.theNewsToken forKey: @"newsToken"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.releaseTime)
        [jsonObject setObject: self.releaseTime forKey: @"releaseTime"];
    if(self.author)
        [jsonObject setObject: self.author forKey: @"author"];
    if(self.sourceDesc)
        [jsonObject setObject: self.sourceDesc forKey: @"sourceDesc"];
    if(self.sourceUrl)
        [jsonObject setObject: self.sourceUrl forKey: @"sourceUrl"];
    if(self.contentUrl)
        [jsonObject setObject: self.contentUrl forKey: @"contentUrl"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.likeCount)
        [jsonObject setObject: self.likeCount forKey: @"likeCount"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.viewCount)
        [jsonObject setObject: self.viewCount forKey: @"viewCount"];
    if(self.likeFlag)
        [jsonObject setObject: self.likeFlag forKey: @"likeFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.theNewsToken = [jsonObject objectForKey: @"newsToken"];
        if(self.theNewsToken && [self.theNewsToken isEqual:[NSNull null]])
            self.theNewsToken = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.releaseTime = [jsonObject objectForKey: @"releaseTime"];
        if(self.releaseTime && [self.releaseTime isEqual:[NSNull null]])
            self.releaseTime = nil;

        self.author = [jsonObject objectForKey: @"author"];
        if(self.author && [self.author isEqual:[NSNull null]])
            self.author = nil;

        self.sourceDesc = [jsonObject objectForKey: @"sourceDesc"];
        if(self.sourceDesc && [self.sourceDesc isEqual:[NSNull null]])
            self.sourceDesc = nil;

        self.sourceUrl = [jsonObject objectForKey: @"sourceUrl"];
        if(self.sourceUrl && [self.sourceUrl isEqual:[NSNull null]])
            self.sourceUrl = nil;

        self.contentUrl = [jsonObject objectForKey: @"contentUrl"];
        if(self.contentUrl && [self.contentUrl isEqual:[NSNull null]])
            self.contentUrl = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.likeCount = [jsonObject objectForKey: @"likeCount"];
        if(self.likeCount && [self.likeCount isEqual:[NSNull null]])
            self.likeCount = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.viewCount = [jsonObject objectForKey: @"viewCount"];
        if(self.viewCount && [self.viewCount isEqual:[NSNull null]])
            self.viewCount = nil;

        self.likeFlag = [jsonObject objectForKey: @"likeFlag"];
        if(self.likeFlag && [self.likeFlag isEqual:[NSNull null]])
            self.likeFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
