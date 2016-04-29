//
// EvhSetMinimumAccountsCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhSetMinimumAccountsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetMinimumAccountsCommand
//

@implementation EvhSetMinimumAccountsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetMinimumAccountsCommand* obj = [EvhSetMinimumAccountsCommand new];
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
    if(self.accounts)
        [jsonObject setObject: self.accounts forKey: @"accounts"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accounts = [jsonObject objectForKey: @"accounts"];
        if(self.accounts && [self.accounts isEqual:[NSNull null]])
            self.accounts = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
