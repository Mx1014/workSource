//
// EvhOrgSearchOrganizationRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgSearchOrganizationRestResponse
//
@interface EvhOrgSearchOrganizationRestResponse : EvhRestResponseBase

// array of EvhOrganizationDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
