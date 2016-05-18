//
// EvhUnassignAccountResponse.m
//
#import "EvhUnassignAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnassignAccountResponse
//

@implementation EvhUnassignAccountResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUnassignAccountResponse* obj = [EvhUnassignAccountResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _accountIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.accountIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.accountIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"accountIds"];
    }
    if(self.accountsCount)
        [jsonObject setObject: self.accountsCount forKey: @"accountsCount"];
    if(self.unassignAccountsCount)
        [jsonObject setObject: self.unassignAccountsCount forKey: @"unassignAccountsCount"];
    if(self.expiredDate)
        [jsonObject setObject: self.expiredDate forKey: @"expiredDate"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"accountIds"];
            for(id itemJson in jsonArray) {
                [self.accountIds addObject: itemJson];
            }
        }
        self.accountsCount = [jsonObject objectForKey: @"accountsCount"];
        if(self.accountsCount && [self.accountsCount isEqual:[NSNull null]])
            self.accountsCount = nil;

        self.unassignAccountsCount = [jsonObject objectForKey: @"unassignAccountsCount"];
        if(self.unassignAccountsCount && [self.unassignAccountsCount isEqual:[NSNull null]])
            self.unassignAccountsCount = nil;

        self.expiredDate = [jsonObject objectForKey: @"expiredDate"];
        if(self.expiredDate && [self.expiredDate isEqual:[NSNull null]])
            self.expiredDate = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
