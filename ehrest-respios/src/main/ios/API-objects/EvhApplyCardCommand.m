//
// EvhApplyCardCommand.m
//
#import "EvhApplyCardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyCardCommand
//

@implementation EvhApplyCardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApplyCardCommand* obj = [EvhApplyCardCommand new];
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
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
    if(self.issuerId)
        [jsonObject setObject: self.issuerId forKey: @"issuerId"];
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

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        self.issuerId = [jsonObject objectForKey: @"issuerId"];
        if(self.issuerId && [self.issuerId isEqual:[NSNull null]])
            self.issuerId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
