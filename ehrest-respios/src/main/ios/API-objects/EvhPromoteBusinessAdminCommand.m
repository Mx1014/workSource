//
// EvhPromoteBusinessAdminCommand.m
//
#import "EvhPromoteBusinessAdminCommand.h"
#import "EvhItemScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPromoteBusinessAdminCommand
//

@implementation EvhPromoteBusinessAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPromoteBusinessAdminCommand* obj = [EvhPromoteBusinessAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemScopes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.itemScopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhItemScope* item in self.itemScopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"itemScopes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemScopes"];
            for(id itemJson in jsonArray) {
                EvhItemScope* item = [EvhItemScope new];
                
                [item fromJson: itemJson];
                [self.itemScopes addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
