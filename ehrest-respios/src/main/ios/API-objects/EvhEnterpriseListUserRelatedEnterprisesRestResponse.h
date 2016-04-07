//
// EvhEnterpriseListUserRelatedEnterprisesRestResponse.h
// generated at 2016-04-07 10:47:33 
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
