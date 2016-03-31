//
// EvhOrgListUserRelatedOrganizationsRestResponse.h
// generated at 2016-03-31 15:43:24 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListUserRelatedOrganizationsRestResponse
//
@interface EvhOrgListUserRelatedOrganizationsRestResponse : EvhRestResponseBase

// array of EvhOrganizationSimpleDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
