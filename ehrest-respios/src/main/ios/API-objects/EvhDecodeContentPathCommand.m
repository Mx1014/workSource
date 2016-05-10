//
// EvhDecodeContentPathCommand.m
//
#import "EvhDecodeContentPathCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDecodeContentPathCommand
//

@implementation EvhDecodeContentPathCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDecodeContentPathCommand* obj = [EvhDecodeContentPathCommand new];
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
    if(self.path)
        [jsonObject setObject: self.path forKey: @"path"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.path = [jsonObject objectForKey: @"path"];
        if(self.path && [self.path isEqual:[NSNull null]])
            self.path = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
