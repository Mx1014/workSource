//
// EvhActivityRejectCommand.m
//
#import "EvhActivityRejectCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityRejectCommand
//

@implementation EvhActivityRejectCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityRejectCommand* obj = [EvhActivityRejectCommand new];
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
    if(self.rosterId)
        [jsonObject setObject: self.rosterId forKey: @"rosterId"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.reason)
        [jsonObject setObject: self.reason forKey: @"reason"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rosterId = [jsonObject objectForKey: @"rosterId"];
        if(self.rosterId && [self.rosterId isEqual:[NSNull null]])
            self.rosterId = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.reason = [jsonObject objectForKey: @"reason"];
        if(self.reason && [self.reason isEqual:[NSNull null]])
            self.reason = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
