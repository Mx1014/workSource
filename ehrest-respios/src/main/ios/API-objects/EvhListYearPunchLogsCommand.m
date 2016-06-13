//
// EvhListYearPunchLogsCommand.m
//
#import "EvhListYearPunchLogsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListYearPunchLogsCommand
//

@implementation EvhListYearPunchLogsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListYearPunchLogsCommand* obj = [EvhListYearPunchLogsCommand new];
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
    if(self.queryYear)
        [jsonObject setObject: self.queryYear forKey: @"queryYear"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.queryYear = [jsonObject objectForKey: @"queryYear"];
        if(self.queryYear && [self.queryYear isEqual:[NSNull null]])
            self.queryYear = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
