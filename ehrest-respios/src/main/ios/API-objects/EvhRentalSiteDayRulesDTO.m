//
// EvhRentalSiteDayRulesDTO.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhRentalSiteDayRulesDTO.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDayRulesDTO
//

@implementation EvhRentalSiteDayRulesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteDayRulesDTO* obj = [EvhRentalSiteDayRulesDTO new];
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
        for(EvhRentalSiteRulesDTO* item in self.siteRules) {
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
                EvhRentalSiteRulesDTO* item = [EvhRentalSiteRulesDTO new];
                
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
