//
// EvhQueryEnterpriseByPhoneCommand.m
//
#import "EvhQueryEnterpriseByPhoneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryEnterpriseByPhoneCommand
//

@implementation EvhQueryEnterpriseByPhoneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryEnterpriseByPhoneCommand* obj = [EvhQueryEnterpriseByPhoneCommand new];
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
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
