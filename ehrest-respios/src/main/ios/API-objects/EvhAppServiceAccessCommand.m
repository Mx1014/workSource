//
// EvhAppServiceAccessCommand.m
//
#import "EvhAppServiceAccessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppServiceAccessCommand
//

@implementation EvhAppServiceAccessCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppServiceAccessCommand* obj = [EvhAppServiceAccessCommand new];
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
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.uri)
        [jsonObject setObject: self.uri forKey: @"uri"];
    if(self.type)
        [jsonObject setObject: self.type forKey: @"type"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.uri = [jsonObject objectForKey: @"uri"];
        if(self.uri && [self.uri isEqual:[NSNull null]])
            self.uri = nil;

        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
