//
// EvhActivitySignupCommand.m
//
#import "EvhActivitySignupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivitySignupCommand
//

@implementation EvhActivitySignupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivitySignupCommand* obj = [EvhActivitySignupCommand new];
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
    if(self.adultCount)
        [jsonObject setObject: self.adultCount forKey: @"adultCount"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.activityId = [jsonObject objectForKey: @"activityId"];
        if(self.activityId && [self.activityId isEqual:[NSNull null]])
            self.activityId = nil;

        self.adultCount = [jsonObject objectForKey: @"adultCount"];
        if(self.adultCount && [self.adultCount isEqual:[NSNull null]])
            self.adultCount = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
