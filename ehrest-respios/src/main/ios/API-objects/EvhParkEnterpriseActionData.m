//
// EvhParkEnterpriseActionData.m
//
#import "EvhParkEnterpriseActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkEnterpriseActionData
//

@implementation EvhParkEnterpriseActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkEnterpriseActionData* obj = [EvhParkEnterpriseActionData new];
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
    if(self.type)
        [jsonObject setObject: self.type forKey: @"type"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
