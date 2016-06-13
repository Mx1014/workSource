//
// EvhAppVersionCommand.m
//
#import "EvhAppVersionCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppVersionCommand
//

@implementation EvhAppVersionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppVersionCommand* obj = [EvhAppVersionCommand new];
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
    if(self.platformType)
        [jsonObject setObject: self.platformType forKey: @"platformType"];
    if(self.currVersionCode)
        [jsonObject setObject: self.currVersionCode forKey: @"currVersionCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.platformType = [jsonObject objectForKey: @"platformType"];
        if(self.platformType && [self.platformType isEqual:[NSNull null]])
            self.platformType = nil;

        self.currVersionCode = [jsonObject objectForKey: @"currVersionCode"];
        if(self.currVersionCode && [self.currVersionCode isEqual:[NSNull null]])
            self.currVersionCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
