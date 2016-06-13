//
// EvhGetUserPointCommand.m
//
#import "EvhGetUserPointCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserPointCommand
//

@implementation EvhGetUserPointCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserPointCommand* obj = [EvhGetUserPointCommand new];
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
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
