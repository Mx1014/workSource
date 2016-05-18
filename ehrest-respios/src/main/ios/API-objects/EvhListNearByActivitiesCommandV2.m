//
// EvhListNearByActivitiesCommandV2.m
//
#import "EvhListNearByActivitiesCommandV2.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearByActivitiesCommandV2
//

@implementation EvhListNearByActivitiesCommandV2

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNearByActivitiesCommandV2* obj = [EvhListNearByActivitiesCommandV2 new];
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
    if(self.community_id)
        [jsonObject setObject: self.community_id forKey: @"community_id"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.community_id = [jsonObject objectForKey: @"community_id"];
        if(self.community_id && [self.community_id isEqual:[NSNull null]])
            self.community_id = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
