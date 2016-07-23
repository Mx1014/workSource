//
// EvhAddRentalBillCommand.m
//
#import "EvhAddRentalBillCommand.h"
#import "EvhRentalBillRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillCommand
//

@implementation EvhAddRentalBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddRentalBillCommand* obj = [EvhAddRentalBillCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rules = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.rentalDate)
        [jsonObject setObject: self.rentalDate forKey: @"rentalDate"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.rules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalBillRuleDTO* item in self.rules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rules"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.rentalDate = [jsonObject objectForKey: @"rentalDate"];
        if(self.rentalDate && [self.rentalDate isEqual:[NSNull null]])
            self.rentalDate = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rules"];
            for(id itemJson in jsonArray) {
                EvhRentalBillRuleDTO* item = [EvhRentalBillRuleDTO new];
                
                [item fromJson: itemJson];
                [self.rules addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
