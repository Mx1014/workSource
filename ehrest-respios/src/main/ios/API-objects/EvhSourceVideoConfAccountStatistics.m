//
// EvhSourceVideoConfAccountStatistics.m
//
#import "EvhSourceVideoConfAccountStatistics.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSourceVideoConfAccountStatistics
//

@implementation EvhSourceVideoConfAccountStatistics

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSourceVideoConfAccountStatistics* obj = [EvhSourceVideoConfAccountStatistics new];
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
    if(self.monitoringPoints)
        [jsonObject setObject: self.monitoringPoints forKey: @"monitoringPoints"];
    if(self.ratio)
        [jsonObject setObject: self.ratio forKey: @"ratio"];
    if(self.warningLine)
        [jsonObject setObject: self.warningLine forKey: @"warningLine"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.monitoringPoints = [jsonObject objectForKey: @"monitoringPoints"];
        if(self.monitoringPoints && [self.monitoringPoints isEqual:[NSNull null]])
            self.monitoringPoints = nil;

        self.ratio = [jsonObject objectForKey: @"ratio"];
        if(self.ratio && [self.ratio isEqual:[NSNull null]])
            self.ratio = nil;

        self.warningLine = [jsonObject objectForKey: @"warningLine"];
        if(self.warningLine && [self.warningLine isEqual:[NSNull null]])
            self.warningLine = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
