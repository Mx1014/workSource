//
// EvhOrganizationQueryResult.m
//
#import "EvhOrganizationQueryResult.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationQueryResult
//

@implementation EvhOrganizationQueryResult

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationQueryResult* obj = [EvhOrganizationQueryResult new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _ids = [NSMutableArray new];
        _dtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.ids) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.ids) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"ids"];
    }
    if(self.dtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationDTO* item in self.dtos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"dtos"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"ids"];
            for(id itemJson in jsonArray) {
                [self.ids addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"dtos"];
            for(id itemJson in jsonArray) {
                EvhOrganizationDTO* item = [EvhOrganizationDTO new];
                
                [item fromJson: itemJson];
                [self.dtos addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
