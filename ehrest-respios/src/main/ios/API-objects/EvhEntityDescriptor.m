//
// EvhEntityDescriptor.m
//
#import "EvhEntityDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEntityDescriptor
//

@implementation EvhEntityDescriptor

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEntityDescriptor* obj = [EvhEntityDescriptor new];
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
    if(self.entityType)
        [jsonObject setObject: self.entityType forKey: @"entityType"];
    if(self.entityId)
        [jsonObject setObject: self.entityId forKey: @"entityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.entityType = [jsonObject objectForKey: @"entityType"];
        if(self.entityType && [self.entityType isEqual:[NSNull null]])
            self.entityType = nil;

        self.entityId = [jsonObject objectForKey: @"entityId"];
        if(self.entityId && [self.entityId isEqual:[NSNull null]])
            self.entityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
