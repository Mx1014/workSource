//
// EvhSetPrimaryFamilyCommand.m
//
#import "EvhSetPrimaryFamilyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPrimaryFamilyCommand
//

@implementation EvhSetPrimaryFamilyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetPrimaryFamilyCommand* obj = [EvhSetPrimaryFamilyCommand new];
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
