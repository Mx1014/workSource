//
// EvhPropCommunityBillDateCommand.m
//
#import "EvhPropCommunityBillDateCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityBillDateCommand
//

@implementation EvhPropCommunityBillDateCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropCommunityBillDateCommand* obj = [EvhPropCommunityBillDateCommand new];
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
    if(self.dateStr)
        [jsonObject setObject: self.dateStr forKey: @"dateStr"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.dateStr = [jsonObject objectForKey: @"dateStr"];
        if(self.dateStr && [self.dateStr isEqual:[NSNull null]])
            self.dateStr = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
