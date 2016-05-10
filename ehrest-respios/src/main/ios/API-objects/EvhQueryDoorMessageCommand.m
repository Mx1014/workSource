//
// EvhQueryDoorMessageCommand.m
//
#import "EvhQueryDoorMessageCommand.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageCommand
//

@implementation EvhQueryDoorMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryDoorMessageCommand* obj = [EvhQueryDoorMessageCommand new];
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
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.urgent)
        [jsonObject setObject: self.urgent forKey: @"urgent"];
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
        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.urgent = [jsonObject objectForKey: @"urgent"];
        if(self.urgent && [self.urgent isEqual:[NSNull null]])
            self.urgent = nil;

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
