//
// EvhGetCommunityByUuidCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhGetCommunityByUuidCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCommunityByUuidCommand
//

@implementation EvhGetCommunityByUuidCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetCommunityByUuidCommand* obj = [EvhGetCommunityByUuidCommand new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
