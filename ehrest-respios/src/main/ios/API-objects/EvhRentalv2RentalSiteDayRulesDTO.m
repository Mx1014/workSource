//
// EvhRentalv2RentalSiteDayRulesDTO.m
//
#import "EvhRentalv2RentalSiteDayRulesDTO.h"
#import "EvhRentalv2RentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2RentalSiteDayRulesDTO
//

@implementation EvhRentalv2RentalSiteDayRulesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2RentalSiteDayRulesDTO* obj = [EvhRentalv2RentalSiteDayRulesDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteRules = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalDate)
        [jsonObject setObject: self.rentalDate forKey: @"rentalDate"];
    if(self.siteRules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2RentalSiteRulesDTO* item in self.siteRules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteRules"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalDate = [jsonObject objectForKey: @"rentalDate"];
        if(self.rentalDate && [self.rentalDate isEqual:[NSNull null]])
            self.rentalDate = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteRules"];
            for(id itemJson in jsonArray) {
                EvhRentalv2RentalSiteRulesDTO* item = [EvhRentalv2RentalSiteRulesDTO new];
                
                [item fromJson: itemJson];
                [self.siteRules addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
