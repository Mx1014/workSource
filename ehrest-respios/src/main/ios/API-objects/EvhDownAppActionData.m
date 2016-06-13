//
// EvhDownAppActionData.m
//
#import "EvhDownAppActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDownAppActionData
//

@implementation EvhDownAppActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDownAppActionData* obj = [EvhDownAppActionData new];
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
    if(self.iosUrl)
        [jsonObject setObject: self.iosUrl forKey: @"iosUrl"];
    if(self.androidUrl)
        [jsonObject setObject: self.androidUrl forKey: @"androidUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.iosUrl = [jsonObject objectForKey: @"iosUrl"];
        if(self.iosUrl && [self.iosUrl isEqual:[NSNull null]])
            self.iosUrl = nil;

        self.androidUrl = [jsonObject objectForKey: @"androidUrl"];
        if(self.androidUrl && [self.androidUrl isEqual:[NSNull null]])
            self.androidUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
