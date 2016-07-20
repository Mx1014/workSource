//
// EvhPropAddressMappingDTO.m
//
#import "EvhPropAddressMappingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropAddressMappingDTO
//

@implementation EvhPropAddressMappingDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropAddressMappingDTO* obj = [EvhPropAddressMappingDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.addressName)
        [jsonObject setObject: self.addressName forKey: @"addressName"];
    if(self.organizationAddress)
        [jsonObject setObject: self.organizationAddress forKey: @"organizationAddress"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.addressName = [jsonObject objectForKey: @"addressName"];
        if(self.addressName && [self.addressName isEqual:[NSNull null]])
            self.addressName = nil;

        self.organizationAddress = [jsonObject objectForKey: @"organizationAddress"];
        if(self.organizationAddress && [self.organizationAddress isEqual:[NSNull null]])
            self.organizationAddress = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
