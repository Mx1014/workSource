//
// EvhOrganizationMenuResponse.h
// generated at 2016-03-31 15:43:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMenuResponse
//
@interface EvhOrganizationMenuResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhOrganizationDTO* OrganizationMenu;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

