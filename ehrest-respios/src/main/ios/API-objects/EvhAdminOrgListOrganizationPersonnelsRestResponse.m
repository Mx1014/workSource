//
// EvhAdminOrgListOrganizationPersonnelsRestResponse.m
// generated at 2016-03-25 09:26:43 
//
#import "EvhAdminOrgListOrganizationPersonnelsRestResponse.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrganizationPersonnelsRestResponse
//

@implementation EvhAdminOrgListOrganizationPersonnelsRestResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAdminOrgListOrganizationPersonnelsRestResponse* obj = [EvhAdminOrgListOrganizationPersonnelsRestResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _response = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    [super toJson: jsonObject];
    
    if(self.response) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationMemberDTO* item in self.response) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"response"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        NSArray* jsonArray = [jsonObject objectForKey: @"response"];
        for(NSMutableDictionary* dic in jsonArray) {
            EvhOrganizationMemberDTO* item = [EvhOrganizationMemberDTO new];
            [item fromJson:dic];
            [self.response addObject: item];
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
