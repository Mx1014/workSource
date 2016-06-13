//
// EvhListReservationConfResponse.m
//
#import "EvhListReservationConfResponse.h"
#import "EvhConfReservationsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListReservationConfResponse
//

@implementation EvhListReservationConfResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListReservationConfResponse* obj = [EvhListReservationConfResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _reserveConf = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.reserveConf) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhConfReservationsDTO* item in self.reserveConf) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"reserveConf"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"reserveConf"];
            for(id itemJson in jsonArray) {
                EvhConfReservationsDTO* item = [EvhConfReservationsDTO new];
                
                [item fromJson: itemJson];
                [self.reserveConf addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
