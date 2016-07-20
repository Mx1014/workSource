//
// EvhActivityCheckinCommand.m
//
#import "EvhActivityCheckinCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityCheckinCommand
//

@implementation EvhActivityCheckinCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityCheckinCommand* obj = [EvhActivityCheckinCommand new];
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
    if(self.activityId)
        [jsonObject setObject: self.activityId forKey: @"activityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.activityId = [jsonObject objectForKey: @"activityId"];
        if(self.activityId && [self.activityId isEqual:[NSNull null]])
            self.activityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
