//
// EvhFindRentalSitesStatusCommandResponse.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesStatusCommandResponse
//
@interface EvhFindRentalSitesStatusCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalSiteDTO*
@property(nonatomic, strong) NSMutableArray* sites;

@property(nonatomic, copy) NSString* contactNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

