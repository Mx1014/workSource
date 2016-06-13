//
// EvhListUserCommand.m
//
#import "EvhListUserCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserCommand
//

@implementation EvhListUserCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUserCommand* obj = [EvhListUserCommand new];
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
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
