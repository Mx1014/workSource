//
// EvhListEnterpriseByPhoneCommand.m
//
#import "EvhListEnterpriseByPhoneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseByPhoneCommand
//

@implementation EvhListEnterpriseByPhoneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseByPhoneCommand* obj = [EvhListEnterpriseByPhoneCommand new];
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
