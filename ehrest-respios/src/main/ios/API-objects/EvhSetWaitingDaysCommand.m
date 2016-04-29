//
// EvhSetWaitingDaysCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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
