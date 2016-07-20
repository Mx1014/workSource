//
// EvhSyncUserContactCommand.m
//
#import "EvhSyncUserContactCommand.h"
#import "EvhContact.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncUserContactCommand
//

@implementation EvhSyncUserContactCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncUserContactCommand* obj = [EvhSyncUserContactCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _contacts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.contacts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhContact* item in self.contacts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"contacts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contacts"];
            for(id itemJson in jsonArray) {
                EvhContact* item = [EvhContact new];
                
                [item fromJson: itemJson];
                [self.contacts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
