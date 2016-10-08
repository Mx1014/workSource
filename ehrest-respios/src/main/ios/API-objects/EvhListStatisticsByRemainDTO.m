//
// EvhListStatisticsByRemainDTO.m
//
#import "EvhListStatisticsByRemainDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByRemainDTO
//

@implementation EvhListStatisticsByRemainDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByRemainDTO* obj = [EvhListStatisticsByRemainDTO new];
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
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.theNewUserCount)
        [jsonObject setObject: self.theNewUserCount forKey: @"newUserCount"];
    if(self.oneDayRemainRatio)
        [jsonObject setObject: self.oneDayRemainRatio forKey: @"oneDayRemainRatio"];
    if(self.twoDaysRemainRatio)
        [jsonObject setObject: self.twoDaysRemainRatio forKey: @"twoDaysRemainRatio"];
    if(self.threeDaysRemainRatio)
        [jsonObject setObject: self.threeDaysRemainRatio forKey: @"threeDaysRemainRatio"];
    if(self.fourDaysRemainRatio)
        [jsonObject setObject: self.fourDaysRemainRatio forKey: @"fourDaysRemainRatio"];
    if(self.fiveDaysRemainRatio)
        [jsonObject setObject: self.fiveDaysRemainRatio forKey: @"fiveDaysRemainRatio"];
    if(self.sixDaysRemainRatio)
        [jsonObject setObject: self.sixDaysRemainRatio forKey: @"sixDaysRemainRatio"];
    if(self.sevenDaysRemainRatio)
        [jsonObject setObject: self.sevenDaysRemainRatio forKey: @"sevenDaysRemainRatio"];
    if(self.fortnightRemainRatio)
        [jsonObject setObject: self.fortnightRemainRatio forKey: @"fortnightRemainRatio"];
    if(self.thirtyDaysRemainRatio)
        [jsonObject setObject: self.thirtyDaysRemainRatio forKey: @"thirtyDaysRemainRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.theNewUserCount = [jsonObject objectForKey: @"newUserCount"];
        if(self.theNewUserCount && [self.theNewUserCount isEqual:[NSNull null]])
            self.theNewUserCount = nil;

        self.oneDayRemainRatio = [jsonObject objectForKey: @"oneDayRemainRatio"];
        if(self.oneDayRemainRatio && [self.oneDayRemainRatio isEqual:[NSNull null]])
            self.oneDayRemainRatio = nil;

        self.twoDaysRemainRatio = [jsonObject objectForKey: @"twoDaysRemainRatio"];
        if(self.twoDaysRemainRatio && [self.twoDaysRemainRatio isEqual:[NSNull null]])
            self.twoDaysRemainRatio = nil;

        self.threeDaysRemainRatio = [jsonObject objectForKey: @"threeDaysRemainRatio"];
        if(self.threeDaysRemainRatio && [self.threeDaysRemainRatio isEqual:[NSNull null]])
            self.threeDaysRemainRatio = nil;

        self.fourDaysRemainRatio = [jsonObject objectForKey: @"fourDaysRemainRatio"];
        if(self.fourDaysRemainRatio && [self.fourDaysRemainRatio isEqual:[NSNull null]])
            self.fourDaysRemainRatio = nil;

        self.fiveDaysRemainRatio = [jsonObject objectForKey: @"fiveDaysRemainRatio"];
        if(self.fiveDaysRemainRatio && [self.fiveDaysRemainRatio isEqual:[NSNull null]])
            self.fiveDaysRemainRatio = nil;

        self.sixDaysRemainRatio = [jsonObject objectForKey: @"sixDaysRemainRatio"];
        if(self.sixDaysRemainRatio && [self.sixDaysRemainRatio isEqual:[NSNull null]])
            self.sixDaysRemainRatio = nil;

        self.sevenDaysRemainRatio = [jsonObject objectForKey: @"sevenDaysRemainRatio"];
        if(self.sevenDaysRemainRatio && [self.sevenDaysRemainRatio isEqual:[NSNull null]])
            self.sevenDaysRemainRatio = nil;

        self.fortnightRemainRatio = [jsonObject objectForKey: @"fortnightRemainRatio"];
        if(self.fortnightRemainRatio && [self.fortnightRemainRatio isEqual:[NSNull null]])
            self.fortnightRemainRatio = nil;

        self.thirtyDaysRemainRatio = [jsonObject objectForKey: @"thirtyDaysRemainRatio"];
        if(self.thirtyDaysRemainRatio && [self.thirtyDaysRemainRatio isEqual:[NSNull null]])
            self.thirtyDaysRemainRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
