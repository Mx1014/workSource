//
// EvhDownloadAppCommand.m
//
#import "EvhDownloadAppCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDownloadAppCommand
//

@implementation EvhDownloadAppCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDownloadAppCommand* obj = [EvhDownloadAppCommand new];
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
    if(self.appType)
        [jsonObject setObject: self.appType forKey: @"appType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appType = [jsonObject objectForKey: @"appType"];
        if(self.appType && [self.appType isEqual:[NSNull null]])
            self.appType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
