//
// EvhRealEstatePostCommand.m
// generated at 2016-03-25 09:26:39 
//
#import "EvhRealEstatePostCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRealEstatePostCommand
//

@implementation EvhRealEstatePostCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRealEstatePostCommand* obj = [EvhRealEstatePostCommand new];
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
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
