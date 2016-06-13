//
// EvhListStatisticsByGenderDTO.m
//
#import "EvhListStatisticsByGenderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByGenderDTO
//

@implementation EvhListStatisticsByGenderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByGenderDTO* obj = [EvhListStatisticsByGenderDTO new];
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
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.registerConut)
        [jsonObject setObject: self.registerConut forKey: @"registerConut"];
    if(self.addressCount)
        [jsonObject setObject: self.addressCount forKey: @"addressCount"];
    if(self.addrRatio)
        [jsonObject setObject: self.addrRatio forKey: @"addrRatio"];
    if(self.genderRegRatio)
        [jsonObject setObject: self.genderRegRatio forKey: @"genderRegRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        self.registerConut = [jsonObject objectForKey: @"registerConut"];
        if(self.registerConut && [self.registerConut isEqual:[NSNull null]])
            self.registerConut = nil;

        self.addressCount = [jsonObject objectForKey: @"addressCount"];
        if(self.addressCount && [self.addressCount isEqual:[NSNull null]])
            self.addressCount = nil;

        self.addrRatio = [jsonObject objectForKey: @"addrRatio"];
        if(self.addrRatio && [self.addrRatio isEqual:[NSNull null]])
            self.addrRatio = nil;

        self.genderRegRatio = [jsonObject objectForKey: @"genderRegRatio"];
        if(self.genderRegRatio && [self.genderRegRatio isEqual:[NSNull null]])
            self.genderRegRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
