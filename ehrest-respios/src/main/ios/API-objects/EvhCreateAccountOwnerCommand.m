//
// EvhCreateAccountOwnerCommand.m
//
#import "EvhCreateAccountOwnerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateAccountOwnerCommand
//

@implementation EvhCreateAccountOwnerCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateAccountOwnerCommand* obj = [EvhCreateAccountOwnerCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _accountIds = [NSMutableArray new];
        _userIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.accountIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.accountIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"accountIds"];
    }
    if(self.userIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.userIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"userIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"accountIds"];
            for(id itemJson in jsonArray) {
                [self.accountIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"userIds"];
            for(id itemJson in jsonArray) {
                [self.userIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
