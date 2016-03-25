//
// EvhFindRentalSitesStatusCommandResponse.h
// generated at 2016-03-25 11:43:32 
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

