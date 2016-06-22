//
// EvhFindRentalSitesCommandResponse.m
//
#import "EvhFindRentalSitesCommandResponse.h"
#import "EvhRentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesCommandResponse
//

@implementation EvhFindRentalSitesCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSitesCommandResponse* obj = [EvhFindRentalSitesCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalSites = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.rentalSites) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSiteDTO* item in self.rentalSites) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalSites"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalSites"];
            for(id itemJson in jsonArray) {
                EvhRentalSiteDTO* item = [EvhRentalSiteDTO new];
                
                [item fromJson: itemJson];
                [self.rentalSites addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
