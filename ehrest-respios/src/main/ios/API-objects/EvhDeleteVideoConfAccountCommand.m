//
// EvhDeleteVideoConfAccountCommand.m
//
#import "EvhDeleteVideoConfAccountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteVideoConfAccountCommand
//

@implementation EvhDeleteVideoConfAccountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteVideoConfAccountCommand* obj = [EvhDeleteVideoConfAccountCommand new];
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
    if(self.accountId)
        [jsonObject setObject: self.accountId forKey: @"accountId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accountId = [jsonObject objectForKey: @"accountId"];
        if(self.accountId && [self.accountId isEqual:[NSNull null]])
            self.accountId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
