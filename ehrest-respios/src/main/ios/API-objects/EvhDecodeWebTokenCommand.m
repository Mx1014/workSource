//
// EvhDecodeWebTokenCommand.m
//
#import "EvhDecodeWebTokenCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDecodeWebTokenCommand
//

@implementation EvhDecodeWebTokenCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDecodeWebTokenCommand* obj = [EvhDecodeWebTokenCommand new];
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
    if(self.webToken)
        [jsonObject setObject: self.webToken forKey: @"webToken"];
    if(self.tokenType)
        [jsonObject setObject: self.tokenType forKey: @"tokenType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.webToken = [jsonObject objectForKey: @"webToken"];
        if(self.webToken && [self.webToken isEqual:[NSNull null]])
            self.webToken = nil;

        self.tokenType = [jsonObject objectForKey: @"tokenType"];
        if(self.tokenType && [self.tokenType isEqual:[NSNull null]])
            self.tokenType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
