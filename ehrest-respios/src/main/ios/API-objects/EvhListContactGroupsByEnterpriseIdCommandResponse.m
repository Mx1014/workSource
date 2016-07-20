//
// EvhListContactGroupsByEnterpriseIdCommandResponse.m
//
#import "EvhListContactGroupsByEnterpriseIdCommandResponse.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactGroupsByEnterpriseIdCommandResponse
//

@implementation EvhListContactGroupsByEnterpriseIdCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListContactGroupsByEnterpriseIdCommandResponse* obj = [EvhListContactGroupsByEnterpriseIdCommandResponse new];
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
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
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
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

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
