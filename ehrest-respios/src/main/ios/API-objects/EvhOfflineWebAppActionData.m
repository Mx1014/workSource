//
// EvhOfflineWebAppActionData.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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
