//
// EvhOfflineWebAppActionData.m
//
#import "EvhOfflineWebAppActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOfflineWebAppActionData
//

@implementation EvhOfflineWebAppActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOfflineWebAppActionData* obj = [EvhOfflineWebAppActionData new];
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
    if(self.realm)
        [jsonObject setObject: self.realm forKey: @"realm"];
    if(self.entryUrl)
        [jsonObject setObject: self.entryUrl forKey: @"entryUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.realm = [jsonObject objectForKey: @"realm"];
        if(self.realm && [self.realm isEqual:[NSNull null]])
            self.realm = nil;

        self.entryUrl = [jsonObject objectForKey: @"entryUrl"];
        if(self.entryUrl && [self.entryUrl isEqual:[NSNull null]])
            self.entryUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
