//
// EvhListUserByIdentifierCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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
