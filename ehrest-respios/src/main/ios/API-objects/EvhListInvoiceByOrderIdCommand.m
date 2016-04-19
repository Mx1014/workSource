//
// EvhListInvoiceByOrderIdCommand.m
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import "EvhListInvoiceByOrderIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvoiceByOrderIdCommand
//

@implementation EvhListInvoiceByOrderIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListInvoiceByOrderIdCommand* obj = [EvhListInvoiceByOrderIdCommand new];
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
