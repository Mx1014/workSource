//
// EvhUserInfoFroBiz.m
//
#import "EvhUserInfoFroBiz.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInfoFroBiz
//

@implementation EvhUserInfoFroBiz

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserInfoFroBiz* obj = [EvhUserInfoFroBiz new];
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
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.namespaceUserToken)
        [jsonObject setObject: self.namespaceUserToken forKey: @"namespaceUserToken"];
    if(self.telePhone)
        [jsonObject setObject: self.telePhone forKey: @"telePhone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.namespaceUserToken = [jsonObject objectForKey: @"namespaceUserToken"];
        if(self.namespaceUserToken && [self.namespaceUserToken isEqual:[NSNull null]])
            self.namespaceUserToken = nil;

        self.telePhone = [jsonObject objectForKey: @"telePhone"];
        if(self.telePhone && [self.telePhone isEqual:[NSNull null]])
            self.telePhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
