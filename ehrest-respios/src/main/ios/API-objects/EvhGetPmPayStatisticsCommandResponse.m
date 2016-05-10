//
// EvhGetPmPayStatisticsCommandResponse.m
//
#import "EvhGetPmPayStatisticsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPmPayStatisticsCommandResponse
//

@implementation EvhGetPmPayStatisticsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetPmPayStatisticsCommandResponse* obj = [EvhGetPmPayStatisticsCommandResponse new];
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
    if(self.yearIncomeAmount)
        [jsonObject setObject: self.yearIncomeAmount forKey: @"yearIncomeAmount"];
    if(self.unPayAmount)
        [jsonObject setObject: self.unPayAmount forKey: @"unPayAmount"];
    if(self.oweFamilyCount)
        [jsonObject setObject: self.oweFamilyCount forKey: @"oweFamilyCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.yearIncomeAmount = [jsonObject objectForKey: @"yearIncomeAmount"];
        if(self.yearIncomeAmount && [self.yearIncomeAmount isEqual:[NSNull null]])
            self.yearIncomeAmount = nil;

        self.unPayAmount = [jsonObject objectForKey: @"unPayAmount"];
        if(self.unPayAmount && [self.unPayAmount isEqual:[NSNull null]])
            self.unPayAmount = nil;

        self.oweFamilyCount = [jsonObject objectForKey: @"oweFamilyCount"];
        if(self.oweFamilyCount && [self.oweFamilyCount isEqual:[NSNull null]])
            self.oweFamilyCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
