//
// EvhListUserByIdentifierCommand.m
//
#import "EvhListUserByIdentifierCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserByIdentifierCommand
//

@implementation EvhListUserByIdentifierCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUserByIdentifierCommand* obj = [EvhListUserByIdentifierCommand new];
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
    if(self.identifier)
        [jsonObject setObject: self.identifier forKey: @"identifier"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.identifier = [jsonObject objectForKey: @"identifier"];
        if(self.identifier && [self.identifier isEqual:[NSNull null]])
            self.identifier = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
