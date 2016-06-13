//
// EvhSearchBillsOrdersCommand.m
//
#import "EvhSearchBillsOrdersCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchBillsOrdersCommand
//

@implementation EvhSearchBillsOrdersCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchBillsOrdersCommand* obj = [EvhSearchBillsOrdersCommand new];
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
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userContact)
        [jsonObject setObject: self.userContact forKey: @"userContact"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userContact = [jsonObject objectForKey: @"userContact"];
        if(self.userContact && [self.userContact isEqual:[NSNull null]])
            self.userContact = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
