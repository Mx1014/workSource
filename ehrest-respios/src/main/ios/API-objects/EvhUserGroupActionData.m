//
// EvhUserGroupActionData.m
// generated at 2016-03-25 09:26:38 
//
#import "EvhUserGroupActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGroupActionData
//

@implementation EvhUserGroupActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserGroupActionData* obj = [EvhUserGroupActionData new];
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
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
