//
// EvhGetCurrentFirmwareCommand.m
//
#import "EvhGetCurrentFirmwareCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCurrentFirmwareCommand
//

@implementation EvhGetCurrentFirmwareCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetCurrentFirmwareCommand* obj = [EvhGetCurrentFirmwareCommand new];
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
    if(self.firmwareType)
        [jsonObject setObject: self.firmwareType forKey: @"firmwareType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.firmwareType = [jsonObject objectForKey: @"firmwareType"];
        if(self.firmwareType && [self.firmwareType isEqual:[NSNull null]])
            self.firmwareType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
