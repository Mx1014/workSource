//
// EvhGetDoorAccessByHardwareIdCommand.m
//
#import "EvhGetDoorAccessByHardwareIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetDoorAccessByHardwareIdCommand
//

@implementation EvhGetDoorAccessByHardwareIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetDoorAccessByHardwareIdCommand* obj = [EvhGetDoorAccessByHardwareIdCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _hardwareIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.hardwareIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.hardwareIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"hardwareIds"];
    }
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"hardwareIds"];
            for(id itemJson in jsonArray) {
                [self.hardwareIds addObject: itemJson];
            }
        }
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
