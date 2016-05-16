//
// EvhSetWaitingDaysCommand.m
//
#import "EvhSetWaitingDaysCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetWaitingDaysCommand
//

@implementation EvhSetWaitingDaysCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetWaitingDaysCommand* obj = [EvhSetWaitingDaysCommand new];
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
    if(self.days)
        [jsonObject setObject: self.days forKey: @"days"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.days = [jsonObject objectForKey: @"days"];
        if(self.days && [self.days isEqual:[NSNull null]])
            self.days = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
