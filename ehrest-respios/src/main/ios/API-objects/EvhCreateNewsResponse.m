//
// EvhCreateNewsResponse.m
//
#import "EvhCreateNewsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateNewsResponse
//

@implementation EvhCreateNewsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateNewsResponse* obj = [EvhCreateNewsResponse new];
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
    if(self.publishTime)
        [jsonObject setObject: self.publishTime forKey: @"publishTime"];
    if(self.author)
        [jsonObject setObject: self.author forKey: @"author"];
    if(self.sourceDesc)
        [jsonObject setObject: self.sourceDesc forKey: @"sourceDesc"];
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.contentAbstract)
        [jsonObject setObject: self.contentAbstract forKey: @"contentAbstract"];
    if(self.likeCount)
        [jsonObject setObject: self.likeCount forKey: @"likeCount"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.topFlag)
        [jsonObject setObject: self.topFlag forKey: @"topFlag"];
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

        self.publishTime = [jsonObject objectForKey: @"publishTime"];
        if(self.publishTime && [self.publishTime isEqual:[NSNull null]])
            self.publishTime = nil;

        self.author = [jsonObject objectForKey: @"author"];
        if(self.author && [self.author isEqual:[NSNull null]])
            self.author = nil;

        self.sourceDesc = [jsonObject objectForKey: @"sourceDesc"];
        if(self.sourceDesc && [self.sourceDesc isEqual:[NSNull null]])
            self.sourceDesc = nil;

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        self.contentAbstract = [jsonObject objectForKey: @"contentAbstract"];
        if(self.contentAbstract && [self.contentAbstract isEqual:[NSNull null]])
            self.contentAbstract = nil;

        self.likeCount = [jsonObject objectForKey: @"likeCount"];
        if(self.likeCount && [self.likeCount isEqual:[NSNull null]])
            self.likeCount = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.topFlag = [jsonObject objectForKey: @"topFlag"];
        if(self.topFlag && [self.topFlag isEqual:[NSNull null]])
            self.topFlag = nil;

        self.likeFlag = [jsonObject objectForKey: @"likeFlag"];
        if(self.likeFlag && [self.likeFlag isEqual:[NSNull null]])
            self.likeFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
