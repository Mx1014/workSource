//
// EvhOrganizationMenuResponse.h
// generated at 2016-04-29 18:56:01 
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

