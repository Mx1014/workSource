//
// EvhGetDayPunchLogsCommand.m
//
#import "EvhGetDayPunchLogsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetDayPunchLogsCommand
//

@implementation EvhGetDayPunchLogsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetDayPunchLogsCommand* obj = [EvhGetDayPunchLogsCommand new];
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
    if(self.enterpirseId)
        [jsonObject setObject: self.enterpirseId forKey: @"enterpirseId"];
    if(self.queryDate)
        [jsonObject setObject: self.queryDate forKey: @"queryDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpirseId = [jsonObject objectForKey: @"enterpirseId"];
        if(self.enterpirseId && [self.enterpirseId isEqual:[NSNull null]])
            self.enterpirseId = nil;

        self.queryDate = [jsonObject objectForKey: @"queryDate"];
        if(self.queryDate && [self.queryDate isEqual:[NSNull null]])
            self.queryDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
