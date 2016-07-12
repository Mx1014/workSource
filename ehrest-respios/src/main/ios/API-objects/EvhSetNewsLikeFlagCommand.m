//
// EvhSetNewsLikeFlagCommand.m
//
#import "EvhSetNewsLikeFlagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetNewsLikeFlagCommand
//

@implementation EvhSetNewsLikeFlagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetNewsLikeFlagCommand* obj = [EvhSetNewsLikeFlagCommand new];
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
    if(self.theNewsToken)
        [jsonObject setObject: self.theNewsToken forKey: @"newsToken"];
    if(self.likeFlag)
        [jsonObject setObject: self.likeFlag forKey: @"likeFlag"];
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

        self.theNewsToken = [jsonObject objectForKey: @"newsToken"];
        if(self.theNewsToken && [self.theNewsToken isEqual:[NSNull null]])
            self.theNewsToken = nil;

        self.likeFlag = [jsonObject objectForKey: @"likeFlag"];
        if(self.likeFlag && [self.likeFlag isEqual:[NSNull null]])
            self.likeFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
