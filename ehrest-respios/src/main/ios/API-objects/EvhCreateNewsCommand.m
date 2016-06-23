//
// EvhCreateNewsCommand.m
//
#import "EvhCreateNewsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateNewsCommand
//

@implementation EvhCreateNewsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateNewsCommand* obj = [EvhCreateNewsCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.contentAbstract)
        [jsonObject setObject: self.contentAbstract forKey: @"contentAbstract"];
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.author)
        [jsonObject setObject: self.author forKey: @"author"];
    if(self.sourceDesc)
        [jsonObject setObject: self.sourceDesc forKey: @"sourceDesc"];
    if(self.sourceUrl)
        [jsonObject setObject: self.sourceUrl forKey: @"sourceUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.contentAbstract = [jsonObject objectForKey: @"contentAbstract"];
        if(self.contentAbstract && [self.contentAbstract isEqual:[NSNull null]])
            self.contentAbstract = nil;

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.author = [jsonObject objectForKey: @"author"];
        if(self.author && [self.author isEqual:[NSNull null]])
            self.author = nil;

        self.sourceDesc = [jsonObject objectForKey: @"sourceDesc"];
        if(self.sourceDesc && [self.sourceDesc isEqual:[NSNull null]])
            self.sourceDesc = nil;

        self.sourceUrl = [jsonObject objectForKey: @"sourceUrl"];
        if(self.sourceUrl && [self.sourceUrl isEqual:[NSNull null]])
            self.sourceUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
