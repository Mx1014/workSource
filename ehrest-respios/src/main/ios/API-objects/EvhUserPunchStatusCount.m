//
// EvhUserPunchStatusCount.m
//
#import "EvhUserPunchStatusCount.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserPunchStatusCount
//

@implementation EvhUserPunchStatusCount

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserPunchStatusCount* obj = [EvhUserPunchStatusCount new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.count)
        [jsonObject setObject: self.count forKey: @"count"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.count = [jsonObject objectForKey: @"count"];
        if(self.count && [self.count isEqual:[NSNull null]])
            self.count = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
