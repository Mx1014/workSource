//
// EvhListUsersWithoutVideoConfPrivilegeResponse.m
//
#import "EvhListUsersWithoutVideoConfPrivilegeResponse.h"
#import "EvhEnterpriseUsersDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUsersWithoutVideoConfPrivilegeResponse
//

@implementation EvhListUsersWithoutVideoConfPrivilegeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUsersWithoutVideoConfPrivilegeResponse* obj = [EvhListUsersWithoutVideoConfPrivilegeResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _enterpriseUsers = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.enterpriseUsers) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseUsersDTO* item in self.enterpriseUsers) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"enterpriseUsers"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"enterpriseUsers"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseUsersDTO* item = [EvhEnterpriseUsersDTO new];
                
                [item fromJson: itemJson];
                [self.enterpriseUsers addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
