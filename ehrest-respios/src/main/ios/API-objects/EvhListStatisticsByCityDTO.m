//
// EvhListStatisticsByCityDTO.m
//
#import "EvhListStatisticsByCityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByCityDTO
//

@implementation EvhListStatisticsByCityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByCityDTO* obj = [EvhListStatisticsByCityDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.registerConut)
        [jsonObject setObject: self.registerConut forKey: @"registerConut"];
    if(self.activeCount)
        [jsonObject setObject: self.activeCount forKey: @"activeCount"];
    if(self.regRatio)
        [jsonObject setObject: self.regRatio forKey: @"regRatio"];
    if(self.addressCount)
        [jsonObject setObject: self.addressCount forKey: @"addressCount"];
    if(self.addrRatio)
        [jsonObject setObject: self.addrRatio forKey: @"addrRatio"];
    if(self.cityActiveRatio)
        [jsonObject setObject: self.cityActiveRatio forKey: @"cityActiveRatio"];
    if(self.cityRegRatio)
        [jsonObject setObject: self.cityRegRatio forKey: @"cityRegRatio"];
    if(self.cityAddrRatio)
        [jsonObject setObject: self.cityAddrRatio forKey: @"cityAddrRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.registerConut = [jsonObject objectForKey: @"registerConut"];
        if(self.registerConut && [self.registerConut isEqual:[NSNull null]])
            self.registerConut = nil;

        self.activeCount = [jsonObject objectForKey: @"activeCount"];
        if(self.activeCount && [self.activeCount isEqual:[NSNull null]])
            self.activeCount = nil;

        self.regRatio = [jsonObject objectForKey: @"regRatio"];
        if(self.regRatio && [self.regRatio isEqual:[NSNull null]])
            self.regRatio = nil;

        self.addressCount = [jsonObject objectForKey: @"addressCount"];
        if(self.addressCount && [self.addressCount isEqual:[NSNull null]])
            self.addressCount = nil;

        self.addrRatio = [jsonObject objectForKey: @"addrRatio"];
        if(self.addrRatio && [self.addrRatio isEqual:[NSNull null]])
            self.addrRatio = nil;

        self.cityActiveRatio = [jsonObject objectForKey: @"cityActiveRatio"];
        if(self.cityActiveRatio && [self.cityActiveRatio isEqual:[NSNull null]])
            self.cityActiveRatio = nil;

        self.cityRegRatio = [jsonObject objectForKey: @"cityRegRatio"];
        if(self.cityRegRatio && [self.cityRegRatio isEqual:[NSNull null]])
            self.cityRegRatio = nil;

        self.cityAddrRatio = [jsonObject objectForKey: @"cityAddrRatio"];
        if(self.cityAddrRatio && [self.cityAddrRatio isEqual:[NSNull null]])
            self.cityAddrRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
