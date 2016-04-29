//
// EvhDeleteVideoConfAccountCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhDeleteVideoConfAccountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteVideoConfAccountCommand
//

@implementation EvhDeleteVideoConfAccountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteVideoConfAccountCommand* obj = [EvhDeleteVideoConfAccountCommand new];
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
    if(self.accountId)
        [jsonObject setObject: self.accountId forKey: @"accountId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accountId = [jsonObject objectForKey: @"accountId"];
        if(self.accountId && [self.accountId isEqual:[NSNull null]])
            self.accountId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
