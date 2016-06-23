//
// EvhRentalSiteNumberDayRulesDTO.m
//
#import "EvhRentalSiteNumberDayRulesDTO.h"
#import "EvhRentalSiteNumberRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteNumberDayRulesDTO
//

@implementation EvhRentalSiteNumberDayRulesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteNumberDayRulesDTO* obj = [EvhRentalSiteNumberDayRulesDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteNumbers = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalDate)
        [jsonObject setObject: self.rentalDate forKey: @"rentalDate"];
    if(self.siteNumbers) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSiteNumberRuleDTO* item in self.siteNumbers) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteNumbers"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalDate = [jsonObject objectForKey: @"rentalDate"];
        if(self.rentalDate && [self.rentalDate isEqual:[NSNull null]])
            self.rentalDate = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteNumbers"];
            for(id itemJson in jsonArray) {
                EvhRentalSiteNumberRuleDTO* item = [EvhRentalSiteNumberRuleDTO new];
                
                [item fromJson: itemJson];
                [self.siteNumbers addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
