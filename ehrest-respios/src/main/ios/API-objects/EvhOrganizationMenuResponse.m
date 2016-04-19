//
// EvhOrganizationMenuResponse.m
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import "EvhOrganizationMenuResponse.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMenuResponse
//

@implementation EvhOrganizationMenuResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationMenuResponse* obj = [EvhOrganizationMenuResponse new];
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
    if(self.OrganizationMenu) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.OrganizationMenu toJson: dic];
        
        [jsonObject setObject: dic forKey: @"OrganizationMenu"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"OrganizationMenu"];

        self.OrganizationMenu = [EvhOrganizationDTO new];
        self.OrganizationMenu = [self.OrganizationMenu fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
