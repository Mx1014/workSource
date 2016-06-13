//
// EvhApplaunchAppActionData.m
//
#import "EvhApplaunchAppActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplaunchAppActionData
//

@implementation EvhApplaunchAppActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApplaunchAppActionData* obj = [EvhApplaunchAppActionData new];
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
    if(self.pkg)
        [jsonObject setObject: self.pkg forKey: @"pkg"];
    if(self.download)
        [jsonObject setObject: self.download forKey: @"download"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pkg = [jsonObject objectForKey: @"pkg"];
        if(self.pkg && [self.pkg isEqual:[NSNull null]])
            self.pkg = nil;

        self.download = [jsonObject objectForKey: @"download"];
        if(self.download && [self.download isEqual:[NSNull null]])
            self.download = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
