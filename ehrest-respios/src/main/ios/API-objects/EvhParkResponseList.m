//
// EvhParkResponseList.m
//
#import "EvhParkResponseList.h"
#import "EvhParkingChargeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkResponseList
//

@implementation EvhParkResponseList

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkResponseList* obj = [EvhParkResponseList new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _parkingCharge = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.parkingCharge) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhParkingChargeDTO* item in self.parkingCharge) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"parkingCharge"];
    }
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"parkingCharge"];
            for(id itemJson in jsonArray) {
                EvhParkingChargeDTO* item = [EvhParkingChargeDTO new];
                
                [item fromJson: itemJson];
                [self.parkingCharge addObject: item];
            }
        }
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
