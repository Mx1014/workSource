//
// EvhEnterpriseListUserRelatedEnterprisesRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseListUserRelatedEnterprisesRestResponse
//
@interface EvhEnterpriseListUserRelatedEnterprisesRestResponse : EvhRestResponseBase

// array of EvhEnterpriseDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
