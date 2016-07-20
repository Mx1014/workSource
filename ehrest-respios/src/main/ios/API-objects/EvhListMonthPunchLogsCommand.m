//
// EvhListMonthPunchLogsCommand.m
//
#import "EvhListMonthPunchLogsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListMonthPunchLogsCommand
//

@implementation EvhListMonthPunchLogsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListMonthPunchLogsCommand* obj = [EvhListMonthPunchLogsCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.queryTime)
        [jsonObject setObject: self.queryTime forKey: @"queryTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.queryTime = [jsonObject objectForKey: @"queryTime"];
        if(self.queryTime && [self.queryTime isEqual:[NSNull null]])
            self.queryTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
