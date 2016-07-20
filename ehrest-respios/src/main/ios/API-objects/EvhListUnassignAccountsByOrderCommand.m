//
// EvhListUnassignAccountsByOrderCommand.m
//
#import "EvhListUnassignAccountsByOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUnassignAccountsByOrderCommand
//

@implementation EvhListUnassignAccountsByOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUnassignAccountsByOrderCommand* obj = [EvhListUnassignAccountsByOrderCommand new];
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
    if(self.orderId)
        [jsonObject setObject: self.orderId forKey: @"orderId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orderId = [jsonObject objectForKey: @"orderId"];
        if(self.orderId && [self.orderId isEqual:[NSNull null]])
            self.orderId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
