//
// EvhListVideoConfAccountOrderInfoByOrderIdCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhListVideoConfAccountOrderInfoByOrderIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountOrderInfoByOrderIdCommand
//

@implementation EvhListVideoConfAccountOrderInfoByOrderIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListVideoConfAccountOrderInfoByOrderIdCommand* obj = [EvhListVideoConfAccountOrderInfoByOrderIdCommand new];
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
