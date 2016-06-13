//
// EvhGetUserRelatedAddressResponse.m
//
#import "EvhGetUserRelatedAddressResponse.h"
#import "EvhFamilyDTO.h"
#import "EvhOrganizationDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserRelatedAddressResponse
//

@implementation EvhGetUserRelatedAddressResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserRelatedAddressResponse* obj = [EvhGetUserRelatedAddressResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _familyList = [NSMutableArray new];
        _organizationList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.familyList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhFamilyDTO* item in self.familyList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"familyList"];
    }
    if(self.organizationList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationDetailDTO* item in self.organizationList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"organizationList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"familyList"];
            for(id itemJson in jsonArray) {
                EvhFamilyDTO* item = [EvhFamilyDTO new];
                
                [item fromJson: itemJson];
                [self.familyList addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"organizationList"];
            for(id itemJson in jsonArray) {
                EvhOrganizationDetailDTO* item = [EvhOrganizationDetailDTO new];
                
                [item fromJson: itemJson];
                [self.organizationList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
