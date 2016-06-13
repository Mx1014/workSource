//
// EvhFindNewestBillByAddressIdCommand.m
//
#import "EvhFindNewestBillByAddressIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindNewestBillByAddressIdCommand
//

@implementation EvhFindNewestBillByAddressIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindNewestBillByAddressIdCommand* obj = [EvhFindNewestBillByAddressIdCommand new];
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
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
