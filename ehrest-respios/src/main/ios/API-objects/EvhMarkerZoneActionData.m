//
// EvhMarkerZoneActionData.m
//
#import "EvhMarkerZoneActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMarkerZoneActionData
//

@implementation EvhMarkerZoneActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhMarkerZoneActionData* obj = [EvhMarkerZoneActionData new];
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
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
