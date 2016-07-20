//
// EvhAddOrgAddressCommand.m
//
#import "EvhAddOrgAddressCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddOrgAddressCommand
//

@implementation EvhAddOrgAddressCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddOrgAddressCommand* obj = [EvhAddOrgAddressCommand new];
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
    if(self.orgId)
        [jsonObject setObject: self.orgId forKey: @"orgId"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orgId = [jsonObject objectForKey: @"orgId"];
        if(self.orgId && [self.orgId isEqual:[NSNull null]])
            self.orgId = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
