//
// EvhApartmentDTO.m
//
#import "EvhApartmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApartmentDTO
//

@implementation EvhApartmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApartmentDTO* obj = [EvhApartmentDTO new];
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
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.livingStatus)
        [jsonObject setObject: self.livingStatus forKey: @"livingStatus"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.livingStatus = [jsonObject objectForKey: @"livingStatus"];
        if(self.livingStatus && [self.livingStatus isEqual:[NSNull null]])
            self.livingStatus = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
