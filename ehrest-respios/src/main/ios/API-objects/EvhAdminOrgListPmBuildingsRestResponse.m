//
// EvhAdminOrgListPmBuildingsRestResponse.m
//
#import "EvhAdminOrgListPmBuildingsRestResponse.h"
#import "EvhPmBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListPmBuildingsRestResponse
//

@implementation EvhAdminOrgListPmBuildingsRestResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAdminOrgListPmBuildingsRestResponse* obj = [EvhAdminOrgListPmBuildingsRestResponse new];
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
        for(EvhPmBuildingDTO* item in self.response) {
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
            EvhPmBuildingDTO* item = [EvhPmBuildingDTO new];
            [item fromJson:dic];
            [self.response addObject: item];
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
