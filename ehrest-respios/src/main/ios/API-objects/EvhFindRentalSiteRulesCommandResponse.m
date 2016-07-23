//
// EvhFindRentalSiteRulesCommandResponse.m
//
#import "EvhFindRentalSiteRulesCommandResponse.h"
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteRulesCommandResponse
//

@implementation EvhFindRentalSiteRulesCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSiteRulesCommandResponse* obj = [EvhFindRentalSiteRulesCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalSiteRules = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteRules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalRentalSiteRulesDTO* item in self.rentalSiteRules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalSiteRules"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalSiteRules"];
            for(id itemJson in jsonArray) {
                EvhRentalRentalSiteRulesDTO* item = [EvhRentalRentalSiteRulesDTO new];
                
                [item fromJson: itemJson];
                [self.rentalSiteRules addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
