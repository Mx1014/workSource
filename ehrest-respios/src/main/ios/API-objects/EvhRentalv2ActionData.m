//
// EvhRentalv2ActionData.m
//
#import "EvhRentalv2ActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ActionData
//

@implementation EvhRentalv2ActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2ActionData* obj = [EvhRentalv2ActionData new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.entityTag)
        [jsonObject setObject: self.entityTag forKey: @"entityTag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.entityTag = [jsonObject objectForKey: @"entityTag"];
        if(self.entityTag && [self.entityTag isEqual:[NSNull null]])
            self.entityTag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
