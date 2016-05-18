//
// EvhDeleteSourceVideoConfAccountCommand.m
//
#import "EvhDeleteSourceVideoConfAccountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteSourceVideoConfAccountCommand
//

@implementation EvhDeleteSourceVideoConfAccountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteSourceVideoConfAccountCommand* obj = [EvhDeleteSourceVideoConfAccountCommand new];
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
    if(self.sourceAccountId)
        [jsonObject setObject: self.sourceAccountId forKey: @"sourceAccountId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sourceAccountId = [jsonObject objectForKey: @"sourceAccountId"];
        if(self.sourceAccountId && [self.sourceAccountId isEqual:[NSNull null]])
            self.sourceAccountId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
