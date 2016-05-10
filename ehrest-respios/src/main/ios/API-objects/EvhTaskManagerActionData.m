//
// EvhTaskManagerActionData.m
//
#import "EvhTaskManagerActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTaskManagerActionData
//

@implementation EvhTaskManagerActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhTaskManagerActionData* obj = [EvhTaskManagerActionData new];
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
    if(self.module)
        [jsonObject setObject: self.module forKey: @"module"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.module = [jsonObject objectForKey: @"module"];
        if(self.module && [self.module isEqual:[NSNull null]])
            self.module = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
