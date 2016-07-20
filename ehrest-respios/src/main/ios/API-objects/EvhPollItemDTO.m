//
// EvhPollItemDTO.m
//
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollItemDTO
//

@implementation EvhPollItemDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollItemDTO* obj = [EvhPollItemDTO new];
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
    if(self.itemId)
        [jsonObject setObject: self.itemId forKey: @"itemId"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.coverUrl)
        [jsonObject setObject: self.coverUrl forKey: @"coverUrl"];
    if(self.voteCount)
        [jsonObject setObject: self.voteCount forKey: @"voteCount"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemId = [jsonObject objectForKey: @"itemId"];
        if(self.itemId && [self.itemId isEqual:[NSNull null]])
            self.itemId = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.coverUrl = [jsonObject objectForKey: @"coverUrl"];
        if(self.coverUrl && [self.coverUrl isEqual:[NSNull null]])
            self.coverUrl = nil;

        self.voteCount = [jsonObject objectForKey: @"voteCount"];
        if(self.voteCount && [self.voteCount isEqual:[NSNull null]])
            self.voteCount = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
