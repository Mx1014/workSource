//
// EvhOrgListUserRelatedOrganizationsRestResponse.h
// generated at 2016-04-22 13:56:50 
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
