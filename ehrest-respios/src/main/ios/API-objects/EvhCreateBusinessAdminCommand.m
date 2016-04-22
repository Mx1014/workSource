//
// EvhCreateBusinessAdminCommand.m
// generated at 2016-04-22 13:56:46 
//
#import "EvhCreateBusinessAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateBusinessAdminCommand
//

@implementation EvhCreateBusinessAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateBusinessAdminCommand* obj = [EvhCreateBusinessAdminCommand new];
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
