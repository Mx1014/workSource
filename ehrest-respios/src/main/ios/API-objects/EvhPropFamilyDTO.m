//
// EvhPropFamilyDTO.m
//
#import "EvhPropFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropFamilyDTO
//

@implementation EvhPropFamilyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropFamilyDTO* obj = [EvhPropFamilyDTO new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.memberCount)
        [jsonObject setObject: self.memberCount forKey: @"memberCount"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.livingStatus)
        [jsonObject setObject: self.livingStatus forKey: @"livingStatus"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.owed)
        [jsonObject setObject: self.owed forKey: @"owed"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.memberCount = [jsonObject objectForKey: @"memberCount"];
        if(self.memberCount && [self.memberCount isEqual:[NSNull null]])
            self.memberCount = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.livingStatus = [jsonObject objectForKey: @"livingStatus"];
        if(self.livingStatus && [self.livingStatus isEqual:[NSNull null]])
            self.livingStatus = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.owed = [jsonObject objectForKey: @"owed"];
        if(self.owed && [self.owed isEqual:[NSNull null]])
            self.owed = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
