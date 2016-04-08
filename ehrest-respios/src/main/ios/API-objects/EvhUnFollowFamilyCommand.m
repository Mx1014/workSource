//
// EvhUnFollowFamilyCommand.m
// generated at 2016-04-08 20:09:21 
//
#import "EvhUnFollowFamilyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnFollowFamilyCommand
//

@implementation EvhUnFollowFamilyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUnFollowFamilyCommand* obj = [EvhUnFollowFamilyCommand new];
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
    [super toJson: jsonObject];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
