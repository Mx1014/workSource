//
// EvhAclinkCreateDoorAuthListCommand.m
//
#import "EvhAclinkCreateDoorAuthListCommand.h"
#import "EvhCreateDoorAuthCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkCreateDoorAuthListCommand
//

@implementation EvhAclinkCreateDoorAuthListCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkCreateDoorAuthListCommand* obj = [EvhAclinkCreateDoorAuthListCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _auths = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.auths) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCreateDoorAuthCommand* item in self.auths) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"auths"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"auths"];
            for(id itemJson in jsonArray) {
                EvhCreateDoorAuthCommand* item = [EvhCreateDoorAuthCommand new];
                
                [item fromJson: itemJson];
                [self.auths addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
