//
// EvhPaginationCommand.m
//
#import "EvhPaginationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaginationCommand
//

@implementation EvhPaginationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPaginationCommand* obj = [EvhPaginationCommand new];
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
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
