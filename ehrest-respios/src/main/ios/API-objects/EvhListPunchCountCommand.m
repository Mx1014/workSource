//
// EvhListPunchCountCommand.m
//
#import "EvhListPunchCountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchCountCommand
//

@implementation EvhListPunchCountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPunchCountCommand* obj = [EvhListPunchCountCommand new];
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
    if(self.startDay)
        [jsonObject setObject: self.startDay forKey: @"startDay"];
    if(self.endDay)
        [jsonObject setObject: self.endDay forKey: @"endDay"];
    if(self.enterpriseGroupId)
        [jsonObject setObject: self.enterpriseGroupId forKey: @"enterpriseGroupId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.startDay = [jsonObject objectForKey: @"startDay"];
        if(self.startDay && [self.startDay isEqual:[NSNull null]])
            self.startDay = nil;

        self.endDay = [jsonObject objectForKey: @"endDay"];
        if(self.endDay && [self.endDay isEqual:[NSNull null]])
            self.endDay = nil;

        self.enterpriseGroupId = [jsonObject objectForKey: @"enterpriseGroupId"];
        if(self.enterpriseGroupId && [self.enterpriseGroupId isEqual:[NSNull null]])
            self.enterpriseGroupId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
