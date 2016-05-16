//
// EvhRegionDescriptor.m
//
#import "EvhRegionDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionDescriptor
//

@implementation EvhRegionDescriptor

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRegionDescriptor* obj = [EvhRegionDescriptor new];
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
    if(self.regionScope)
        [jsonObject setObject: self.regionScope forKey: @"regionScope"];
    if(self.regionId)
        [jsonObject setObject: self.regionId forKey: @"regionId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.regionScope = [jsonObject objectForKey: @"regionScope"];
        if(self.regionScope && [self.regionScope isEqual:[NSNull null]])
            self.regionScope = nil;

        self.regionId = [jsonObject objectForKey: @"regionId"];
        if(self.regionId && [self.regionId isEqual:[NSNull null]])
            self.regionId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
