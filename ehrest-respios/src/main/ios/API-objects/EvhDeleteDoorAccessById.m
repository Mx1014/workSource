//
// EvhDeleteDoorAccessById.m
//
#import "EvhDeleteDoorAccessById.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteDoorAccessById
//

@implementation EvhDeleteDoorAccessById

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteDoorAccessById* obj = [EvhDeleteDoorAccessById new];
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
    if(self.doorAccessId)
        [jsonObject setObject: self.doorAccessId forKey: @"doorAccessId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.doorAccessId = [jsonObject objectForKey: @"doorAccessId"];
        if(self.doorAccessId && [self.doorAccessId isEqual:[NSNull null]])
            self.doorAccessId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
