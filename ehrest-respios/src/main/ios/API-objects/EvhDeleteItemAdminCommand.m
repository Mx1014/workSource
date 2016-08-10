//
// EvhDeleteItemAdminCommand.m
//
#import "EvhDeleteItemAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteItemAdminCommand
//

@implementation EvhDeleteItemAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteItemAdminCommand* obj = [EvhDeleteItemAdminCommand new];
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
    if(self.itemId)
        [jsonObject setObject: self.itemId forKey: @"itemId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemId = [jsonObject objectForKey: @"itemId"];
        if(self.itemId && [self.itemId isEqual:[NSNull null]])
            self.itemId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
