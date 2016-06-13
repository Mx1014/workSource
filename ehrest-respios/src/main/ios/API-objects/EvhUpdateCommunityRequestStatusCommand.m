//
// EvhUpdateCommunityRequestStatusCommand.m
//
#import "EvhUpdateCommunityRequestStatusCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCommunityRequestStatusCommand
//

@implementation EvhUpdateCommunityRequestStatusCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateCommunityRequestStatusCommand* obj = [EvhUpdateCommunityRequestStatusCommand new];
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
    if(self.requestStatus)
        [jsonObject setObject: self.requestStatus forKey: @"requestStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.requestStatus = [jsonObject objectForKey: @"requestStatus"];
        if(self.requestStatus && [self.requestStatus isEqual:[NSNull null]])
            self.requestStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
