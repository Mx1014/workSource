//
// EvhPropCommunityBillIdCommand.m
//
#import "EvhPropCommunityBillIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityBillIdCommand
//

@implementation EvhPropCommunityBillIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropCommunityBillIdCommand* obj = [EvhPropCommunityBillIdCommand new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
