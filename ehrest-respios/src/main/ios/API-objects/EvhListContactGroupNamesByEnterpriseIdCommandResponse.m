//
// EvhListContactGroupNamesByEnterpriseIdCommandResponse.m
//
#import "EvhListContactGroupNamesByEnterpriseIdCommandResponse.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactGroupNamesByEnterpriseIdCommandResponse
//

@implementation EvhListContactGroupNamesByEnterpriseIdCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListContactGroupNamesByEnterpriseIdCommandResponse* obj = [EvhListContactGroupNamesByEnterpriseIdCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _contactGroups = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.contactGroups) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseContactDTO* item in self.contactGroups) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"contactGroups"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contactGroups"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseContactDTO* item = [EvhEnterpriseContactDTO new];
                
                [item fromJson: itemJson];
                [self.contactGroups addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
