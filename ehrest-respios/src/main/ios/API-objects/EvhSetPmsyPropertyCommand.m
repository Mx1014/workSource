//
// EvhSetPmsyPropertyCommand.m
//
#import "EvhSetPmsyPropertyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPmsyPropertyCommand
//

@implementation EvhSetPmsyPropertyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetPmsyPropertyCommand* obj = [EvhSetPmsyPropertyCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.billTip)
        [jsonObject setObject: self.billTip forKey: @"billTip"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.billTip = [jsonObject objectForKey: @"billTip"];
        if(self.billTip && [self.billTip isEqual:[NSNull null]])
            self.billTip = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
