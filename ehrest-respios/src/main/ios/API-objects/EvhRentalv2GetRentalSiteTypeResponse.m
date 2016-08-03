//
// EvhRentalv2GetRentalSiteTypeResponse.m
//
#import "EvhRentalv2GetRentalSiteTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2GetRentalSiteTypeResponse
//

@implementation EvhRentalv2GetRentalSiteTypeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2GetRentalSiteTypeResponse* obj = [EvhRentalv2GetRentalSiteTypeResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteTypes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.siteTypes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.siteTypes) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"siteTypes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteTypes"];
            for(id itemJson in jsonArray) {
                [self.siteTypes addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
