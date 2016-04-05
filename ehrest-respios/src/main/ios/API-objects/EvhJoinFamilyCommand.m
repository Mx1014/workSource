//
// EvhJoinFamilyCommand.m
// generated at 2016-04-05 13:45:26 
//
#import "EvhJoinFamilyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhJoinFamilyCommand
//

@implementation EvhJoinFamilyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhJoinFamilyCommand* obj = [EvhJoinFamilyCommand new];
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
