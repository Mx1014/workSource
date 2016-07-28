//
// EvhRentalv2FindRentalSitesStatusCommandResponse.m
//
#import "EvhRentalv2FindRentalSitesStatusCommandResponse.h"
#import "EvhRentalv2RentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSitesStatusCommandResponse
//

@implementation EvhRentalv2FindRentalSitesStatusCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2FindRentalSitesStatusCommandResponse* obj = [EvhRentalv2FindRentalSitesStatusCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _sites = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.sites) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2RentalSiteDTO* item in self.sites) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"sites"];
    }
    if(self.contactNum)
        [jsonObject setObject: self.contactNum forKey: @"contactNum"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"sites"];
            for(id itemJson in jsonArray) {
                EvhRentalv2RentalSiteDTO* item = [EvhRentalv2RentalSiteDTO new];
                
                [item fromJson: itemJson];
                [self.sites addObject: item];
            }
        }
        self.contactNum = [jsonObject objectForKey: @"contactNum"];
        if(self.contactNum && [self.contactNum isEqual:[NSNull null]])
            self.contactNum = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
