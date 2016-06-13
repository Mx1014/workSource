//
// EvhListOwningFamilyMembersCommand.m
//
#import "EvhListOwningFamilyMembersCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOwningFamilyMembersCommand
//

@implementation EvhListOwningFamilyMembersCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOwningFamilyMembersCommand* obj = [EvhListOwningFamilyMembersCommand new];
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
