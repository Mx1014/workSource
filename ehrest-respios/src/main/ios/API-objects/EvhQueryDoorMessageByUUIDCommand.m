//
// EvhQueryDoorMessageByUUIDCommand.m
//
#import "EvhQueryDoorMessageByUUIDCommand.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageByUUIDCommand
//

@implementation EvhQueryDoorMessageByUUIDCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryDoorMessageByUUIDCommand* obj = [EvhQueryDoorMessageByUUIDCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _inputs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.inputs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhDoorMessage* item in self.inputs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"inputs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"inputs"];
            for(id itemJson in jsonArray) {
                EvhDoorMessage* item = [EvhDoorMessage new];
                
                [item fromJson: itemJson];
                [self.inputs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
