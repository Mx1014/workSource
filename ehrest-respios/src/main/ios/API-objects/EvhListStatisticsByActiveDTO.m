//
// EvhListStatisticsByActiveDTO.m
//
#import "EvhListStatisticsByActiveDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByActiveDTO
//

@implementation EvhListStatisticsByActiveDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByActiveDTO* obj = [EvhListStatisticsByActiveDTO new];
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
    if(self.yesterdayActiveCount)
        [jsonObject setObject: self.yesterdayActiveCount forKey: @"yesterdayActiveCount"];
    if(self.lastWeekActiveCount)
        [jsonObject setObject: self.lastWeekActiveCount forKey: @"lastWeekActiveCount"];
    if(self.lastMonthActiveCount)
        [jsonObject setObject: self.lastMonthActiveCount forKey: @"lastMonthActiveCount"];
    if(self.ystToLastWeekRatio)
        [jsonObject setObject: self.ystToLastWeekRatio forKey: @"ystToLastWeekRatio"];
    if(self.ystToLastMonthRatio)
        [jsonObject setObject: self.ystToLastMonthRatio forKey: @"ystToLastMonthRatio"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.activeCount)
        [jsonObject setObject: self.activeCount forKey: @"activeCount"];
    if(self.dayActiveToSearchRatio)
        [jsonObject setObject: self.dayActiveToSearchRatio forKey: @"dayActiveToSearchRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.yesterdayActiveCount = [jsonObject objectForKey: @"yesterdayActiveCount"];
        if(self.yesterdayActiveCount && [self.yesterdayActiveCount isEqual:[NSNull null]])
            self.yesterdayActiveCount = nil;

        self.lastWeekActiveCount = [jsonObject objectForKey: @"lastWeekActiveCount"];
        if(self.lastWeekActiveCount && [self.lastWeekActiveCount isEqual:[NSNull null]])
            self.lastWeekActiveCount = nil;

        self.lastMonthActiveCount = [jsonObject objectForKey: @"lastMonthActiveCount"];
        if(self.lastMonthActiveCount && [self.lastMonthActiveCount isEqual:[NSNull null]])
            self.lastMonthActiveCount = nil;

        self.ystToLastWeekRatio = [jsonObject objectForKey: @"ystToLastWeekRatio"];
        if(self.ystToLastWeekRatio && [self.ystToLastWeekRatio isEqual:[NSNull null]])
            self.ystToLastWeekRatio = nil;

        self.ystToLastMonthRatio = [jsonObject objectForKey: @"ystToLastMonthRatio"];
        if(self.ystToLastMonthRatio && [self.ystToLastMonthRatio isEqual:[NSNull null]])
            self.ystToLastMonthRatio = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.activeCount = [jsonObject objectForKey: @"activeCount"];
        if(self.activeCount && [self.activeCount isEqual:[NSNull null]])
            self.activeCount = nil;

        self.dayActiveToSearchRatio = [jsonObject objectForKey: @"dayActiveToSearchRatio"];
        if(self.dayActiveToSearchRatio && [self.dayActiveToSearchRatio isEqual:[NSNull null]])
            self.dayActiveToSearchRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
