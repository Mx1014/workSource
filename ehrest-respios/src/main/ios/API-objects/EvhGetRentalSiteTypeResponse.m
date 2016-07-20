//
// EvhGetRentalSiteTypeResponse.m
//
#import "EvhGetRentalSiteTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetRentalSiteTypeResponse
//

@implementation EvhGetRentalSiteTypeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetRentalSiteTypeResponse* obj = [EvhGetRentalSiteTypeResponse new];
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
