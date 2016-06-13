//
// EvhGetQRCodeImageCommand.m
//
#import "EvhGetQRCodeImageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetQRCodeImageCommand
//

@implementation EvhGetQRCodeImageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetQRCodeImageCommand* obj = [EvhGetQRCodeImageCommand new];
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
    if(self.width)
        [jsonObject setObject: self.width forKey: @"width"];
    if(self.height)
        [jsonObject setObject: self.height forKey: @"height"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.qrid = [jsonObject objectForKey: @"qrid"];
        if(self.qrid && [self.qrid isEqual:[NSNull null]])
            self.qrid = nil;

        self.width = [jsonObject objectForKey: @"width"];
        if(self.width && [self.width isEqual:[NSNull null]])
            self.width = nil;

        self.height = [jsonObject objectForKey: @"height"];
        if(self.height && [self.height isEqual:[NSNull null]])
            self.height = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
