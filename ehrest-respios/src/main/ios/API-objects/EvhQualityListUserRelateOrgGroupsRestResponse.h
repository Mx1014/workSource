//
// EvhQualityListUserRelateOrgGroupsRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListUserRelateOrgGroupsRestResponse
//
@interface EvhQualityListUserRelateOrgGroupsRestResponse : EvhRestResponseBase

// array of EvhOrganizationDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
