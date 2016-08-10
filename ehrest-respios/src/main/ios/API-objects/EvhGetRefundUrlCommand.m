//
// EvhGetRefundUrlCommand.m
//
#import "EvhGetRefundUrlCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetRefundUrlCommand
//

@implementation EvhGetRefundUrlCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetRefundUrlCommand* obj = [EvhGetRefundUrlCommand new];
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
    if(self.refundId)
        [jsonObject setObject: self.refundId forKey: @"refundId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.refundId = [jsonObject objectForKey: @"refundId"];
        if(self.refundId && [self.refundId isEqual:[NSNull null]])
            self.refundId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
