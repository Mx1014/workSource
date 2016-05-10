//
// EvhListApartmentByBuildingNameCommandResponse.m
//
#import "EvhListApartmentByBuildingNameCommandResponse.h"
#import "EvhApartmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListApartmentByBuildingNameCommandResponse
//

@implementation EvhListApartmentByBuildingNameCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListApartmentByBuildingNameCommandResponse* obj = [EvhListApartmentByBuildingNameCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _apartmentList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.apartmentLivingCount)
        [jsonObject setObject: self.apartmentLivingCount forKey: @"apartmentLivingCount"];
    if(self.apartmentList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhApartmentDTO* item in self.apartmentList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"apartmentList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.apartmentLivingCount = [jsonObject objectForKey: @"apartmentLivingCount"];
        if(self.apartmentLivingCount && [self.apartmentLivingCount isEqual:[NSNull null]])
            self.apartmentLivingCount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"apartmentList"];
            for(id itemJson in jsonArray) {
                EvhApartmentDTO* item = [EvhApartmentDTO new];
                
                [item fromJson: itemJson];
                [self.apartmentList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
