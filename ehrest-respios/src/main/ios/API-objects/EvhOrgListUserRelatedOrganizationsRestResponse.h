//
// EvhOrgListUserRelatedOrganizationsRestResponse.h
// generated at 2016-04-18 14:48:52 
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
