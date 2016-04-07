//
// EvhEnterpriseListUserRelatedEnterprisesRestResponse.h
// generated at 2016-04-07 15:16:53 
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
