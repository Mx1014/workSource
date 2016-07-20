//
// EvhGetQRCodeInfoCommand.m
//
#import "EvhGetQRCodeInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetQRCodeInfoCommand
//

@implementation EvhGetQRCodeInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetQRCodeInfoCommand* obj = [EvhGetQRCodeInfoCommand new];
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
    if(self.qrid)
        [jsonObject setObject: self.qrid forKey: @"qrid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.qrid = [jsonObject objectForKey: @"qrid"];
        if(self.qrid && [self.qrid isEqual:[NSNull null]])
            self.qrid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
