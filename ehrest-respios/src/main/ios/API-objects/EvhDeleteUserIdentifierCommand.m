//
// EvhDeleteUserIdentifierCommand.m
//
#import "EvhDeleteUserIdentifierCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteUserIdentifierCommand
//

@implementation EvhDeleteUserIdentifierCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteUserIdentifierCommand* obj = [EvhDeleteUserIdentifierCommand new];
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
    if(self.userIdentifierId)
        [jsonObject setObject: self.userIdentifierId forKey: @"userIdentifierId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userIdentifierId = [jsonObject objectForKey: @"userIdentifierId"];
        if(self.userIdentifierId && [self.userIdentifierId isEqual:[NSNull null]])
            self.userIdentifierId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
